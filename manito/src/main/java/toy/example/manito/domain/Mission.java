package toy.example.manito.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Mission {

    @Id @GeneratedValue
    @Column(name = "mission_id")
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_room_id")
    private UserRoom userRoom;

    public Mission() {
    }

    public Mission(String content, UserRoom userRoom) {
        this.content = content;
        this.userRoom = userRoom;
    }
}
