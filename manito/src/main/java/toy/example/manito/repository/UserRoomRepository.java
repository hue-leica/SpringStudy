package toy.example.manito.repository;

import org.springframework.stereotype.Repository;
import toy.example.manito.domain.Room;
import toy.example.manito.domain.User;
import toy.example.manito.domain.UserRoom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserRoomRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(User user, Room room){
        UserRoom userRoom = new UserRoom(room, user);
        em.persist(userRoom);
    }

    public List<UserRoom> findByRoomId(Long roomId){
        return em.createQuery("select ur from UserRoom ur" +
                " where ur.room.id=:roomId", UserRoom.class)
                .setParameter("roomId", roomId)
                .getResultList();
    }
}
