package toy.example.manito.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Room {

    @Id @GeneratedValue
    @Column(name = "room_id")
    private Long id;
    private LocalDateTime effective_date;
    private LocalDateTime created_date;

    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;

    @OneToMany(mappedBy = "room")
    private List<UserRoom> userRooms = new ArrayList<>();

    public Room() {
    }

    public Room(LocalDateTime effective_date, LocalDateTime created_date, RoomStatus roomStatus) {
        this.effective_date = effective_date;
        this.created_date = created_date;
        this.roomStatus = roomStatus;
    }

    public void changeRoomStatus(RoomStatus roomStatus){
        this.roomStatus = roomStatus;
    }
}
