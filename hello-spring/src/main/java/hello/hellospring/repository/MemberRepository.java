package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id); // 없을 경우 null이 아닌 Optional로 해결 가능
    Optional<Member> findByName(String name);
    List<Member> findAll();
}
