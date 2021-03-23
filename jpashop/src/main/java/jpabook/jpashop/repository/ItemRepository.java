package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Item item){
        /* DB에 들어가기 전까지는 id가 없으니 이것을 체크하기 위함
           --> 자세한 것은 웹 앱 개발에서 설명 */
        if(item.getId() == null)
            em.persist(item);
        else
            em.merge(item); // update라고 지금은 이해 바람
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
