package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByAgeGreaterThan(int age);
    List<Member> getByAgeGreaterThan(int age);

    @Query("select m from Member m")
    List<Member> findAllMember();

    @Query("select m from Member m where m.age > :age")
    List<Member> findAgeMember(@Param("age") int age);
}
