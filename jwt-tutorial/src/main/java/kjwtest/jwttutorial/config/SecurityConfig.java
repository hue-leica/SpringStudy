package kjwtest.jwttutorial.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity // 기본적인 web 보안을 활성화 하는 어노테이션
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests() // http servlet request를 사용하는 모든 요청들에 대해 접근제한을 설정
                .antMatchers("/api/hello").permitAll() // 해당 api는 인증없이 접근을 허용
                .anyRequest().authenticated(); // 나머지 요청에 대해서는 인증 받아야 함
    }

    @Override
    public void configure(WebSecurity web){
        web
                .ignoring()
                .antMatchers( // h2-console과 파비콘 관련은 spring security 수행하지 않게 등록
                        "/h2-console/**",
                        "/favicon.ico"
                );
    }
}
