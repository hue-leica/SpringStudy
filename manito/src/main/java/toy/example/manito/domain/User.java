package toy.example.manito.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String name;
    private String password;

    @OneToMany(mappedBy = "startUser")
    private List<UserRoom> userRooms = new ArrayList<>();
}
