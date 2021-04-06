package toy.example.manito.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class UserRoom {

    @Id @GeneratedValue
    @Column(name = "user_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "start_user_id")
    private User startUser;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "end_user_id")
    private User endUser;

    @OneToMany(mappedBy = "userRoom", cascade = CascadeType.ALL)
    private List<Mission> missions = new ArrayList<>();

    public UserRoom() {
    }

    public UserRoom(Room room, User startUser) {
        this.room = room;
        this.startUser = startUser;
    }

    public void changeEndUser(User user){
        this.endUser = user;
    }
    public void addMission(Mission mission){
        this.missions.add(mission);
    }
}
