package toy.example.manito.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Mission {

    @Id @GeneratedValue
    @Column(name = "mission_id")
    private Long id;

    private String content;
}
