package toy.example.manito.repository;

import org.springframework.stereotype.Repository;
import toy.example.manito.domain.Room;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RoomRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(Room room){
        em.persist(room);
    }

    public Room findOne(Long id){
        return em.find(Room.class, id);
    }
}
