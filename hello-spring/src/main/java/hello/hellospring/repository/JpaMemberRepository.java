package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository{
    // JPA는 EntityManager를 통해서 관리 (안에 커넥션정보 등 이런게 있음)
    private final EntityManager em;
    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }
    @Override
    public Member save(Member member) {
         em.persist(member);
         return member;
    }
    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }
    @Override
    public Optional<Member> findByName(String name) {
        /* JPQL이라는 객체지향 쿼리를 사용한다 - PK기반이 아닌 쿼리는 JPQL을 사용해야한다! */
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();
    }
    @Override
    public List<Member> findAll() {
        /* JPQL이라는 객체지향 쿼리를 사용한다 */
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
