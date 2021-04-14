package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    /* 저장(Create) */
    public Member save(Member member){
        em.persist(member);
        return member;
    }

    /* 단건 조회(Read) */
    public Member findOne(Long id){
        return em.find(Member.class, id);
    }
    /* 모든 건 조회(Read) */
    public List<Member> findAll(){
        return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }
    /* 삭제(Delete) */
    public void delete(Member member){
        em.remove(member);
    }


    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }
    public Long count(){
        return em.createQuery("select count(m) from Member m",Long.class)
                .getSingleResult();
    }

    /* 순수 페이징 & 정렬 구현 */
    public List<Member> findByPage(int age, int offset, int limit){
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc",Member.class)
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
    /* 전체 데이터수를 가져오는 totalCount */
    public long totalCount(int age){
        return em.createQuery("select count(m) from Member m where m.age = :age",Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    public List<Member> findByUsername(String username){
        return em.createNamedQuery("Member.findByUsername",Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    /* age 이상의 나이를 가진 Member의 나이를 모두 update! */
    public int bulkAgePlus(int age){
        return em.createQuery("update Member m set m.age = m.age+1 where m.age >= :age")
                .setParameter("age",age)
                .executeUpdate(); // 벌크성 쿼리는 executeUpdte로 수행함
    }
}

