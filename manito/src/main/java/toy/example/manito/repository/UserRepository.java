package toy.example.manito.repository;

import org.springframework.stereotype.Repository;
import toy.example.manito.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserRepository {
    @PersistenceContext
    private EntityManager em;

    public Long save(User user){
        em.persist(user);
        return em.find(User.class, user.getId()).getId();
    }

    public User findOne(Long id){
        return em.find(User.class, id);
    }

    public List<User> findAll(){
        return em.createQuery("select u" +
                " from User u", User.class)
                .getResultList();
    }

    public List<User> findByName(String name){
        return em.createQuery("select u from User u where u.name=:name",User.class)
                .setParameter("name", name)
                .getResultList();
    }

}
