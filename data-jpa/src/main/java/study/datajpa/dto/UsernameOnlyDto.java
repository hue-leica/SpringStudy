package study.datajpa.dto;

import lombok.Getter;

@Getter
public class UsernameOnlyDto {

    private final String username;

    /* 생성자의 파라미터 이름으로 매칭된다 */
    public UsernameOnlyDto(String username) {
        this.username = username;
    }
}
