package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

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

    /* Optional + 단건 반환 */
    Optional<Member> findByUsername(String username);

    @Modifying // executeUpdate를 실행하기 위한 어노테이션 --> 안하면 조회를 위한 getResultList()나 getSingleResult()호출
    @Query("update Member m set m.age = m.age+1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
    
}
