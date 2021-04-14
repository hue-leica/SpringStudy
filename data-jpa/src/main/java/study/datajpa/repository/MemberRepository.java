package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import javax.persistence.QueryHint;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Page<Member> findByAge(@Param("age") int age, Pageable pageable);

    @Query("select m from Member m where m.username in :usernames")
    List<Member> findByUsernames(@Param("usernames") List<String> usernames);

    /* 특정 Age보다 큰 Member를 반환 */
    List<Member> findByAgeGreaterThan(int age);
    /* 특정 Age인 Member가 있는지 조회 */
    Boolean existsByAge(int age);
    /* 전체 Member 조회 */
    List<Member> findBy();

    /* 컬렉션 반환 */
    //List<Member> findByUsername(String username);

    /* 단건 반환 */
    //Member findByUsername(String username);

    // executeUpdate를 실행하기 위한 어노테이션 --> 안하면 조회를 위한 getResultList()나 getSingleResult()호출
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age+1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    /* JPQL을 이용해 직접 fetch join 쿼리 작성 */
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    /* @EntityGraph를 써서 fetch join 작성 */
    // 1) @EntityGraph + @Query
    // --> 내가 원하는 메소드 이름으로 지정가능
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // 2) @EntityGraph + @Override
    // --> findAll은 JpaRepository에 이미 존재하는 CRUD중 하나라서 재정의 해야 한다!
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    // 3) @EntityGraph + 쿼리 메소드
    // --> 쿼리메소드로 특정 필드를 통해 만들어 주는 것이라서 함께 쓸 수 있다(미리 정의 X)
    @EntityGraph(attributePaths = {"team"})
    List<Member> findByUsername(String username);

    /* QueryHint를 사용해서 불필요한 과정을 최적화 */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    /* Page 사용시 추가 count쿼리에도 적용하는 방법 */
    @QueryHints(value = {@QueryHint(name = "org.hibernate.readOnly", value = "true")},
    forCounting = true)
    Page<Member> findByUsername(String name, Pageable pageable);

}
