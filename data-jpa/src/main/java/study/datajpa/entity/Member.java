package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity @Getter
@ToString(of = {"id", "username", "age"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    // Spring Data JPA를 사용할 때 N+1문제를 해결하기 위한 방법은 차차 설명할 예정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    // Entity는 기본적으로 default 생성자를 가져야하며
    // 생성을 제한하기 위해 protected로 사용하는것을 권장
    // 이것 대신 AccessLevel.PROTECTED로 설정해도 된
//    protected Member() {
//    }

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        this.team = team;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    /* 연관관계 메서드 */
    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }
}
