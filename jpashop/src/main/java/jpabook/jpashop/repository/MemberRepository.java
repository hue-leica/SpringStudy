package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository // @Component 포함하므로 컴포넌트 스캔으로 스캔되어 스프링 빈으로 관리됨
public class MemberRepository {
    // 이 부분도 Spring data jpa를 쓰면 @Autowired로 대체할수 있어서 롬복을 쓸 수 있음
    @PersistenceContext
    private EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
