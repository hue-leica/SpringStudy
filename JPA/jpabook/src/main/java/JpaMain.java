import domain.Address;
import domain.AddressEntity;
import domain.Member;
import domain.item.Book;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            /* 엔티티 프로젝션 -> 대상이 Entity인것
               모든 결과의 객체들은 영속성 컨텍스트에 의해 관리된다
                --> 수정하면 바로 update query가 나간다 */
             em.createQuery("select m from Member m where m.name=?1", Member.class)
                     .setParameter(1,"정욱").getResultList();


            tx.commit();
        }catch(Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
