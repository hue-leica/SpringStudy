package study.datajpa.dto;


import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {
    /* 스프링 SpEL 문법 */
    @Value("#{target.username + ' ' + target.age + ' ' + target.team.name}")
    String getUsername();
}
