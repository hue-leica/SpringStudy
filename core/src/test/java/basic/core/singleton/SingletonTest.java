package basic.core.singleton;

import basic.core.AppConfig;
import basic.core.member.MemberService;
import basic.core.member.MemberServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SingletonTest {

    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너")
    public void pureContainer(){
        /* ApplicationContext(스프링 컨테이)에서 빼지 않았기 때문에 사용할 때 마다
         새로운 객체가 생성되게 될 것이다. */
        AppConfig appConfig = new AppConfig();
        // 조회 1
        MemberService memberService1 = appConfig.memberService();
        // 조회 2
        MemberService memberService2 = appConfig.memberService();
        // 두 객체는 다름 --> 사용자의 요청이 있을 때 마다 객체를 새로 생성함 --> 비효율적!
        Assertions.assertThat(memberService1).isNotSameAs(memberService2);
    }

    @Test
    @DisplayName("싱글톤 패턴을 적용한 객체 사용")
    public void singletonServiceTest(){
        SingletonService instance1 = SingletonService.getInstance();
        SingletonService instance2 = SingletonService.getInstance();

        // 같은 객체를 반환한다 --> 싱글톤 적용 성공!
        Assertions.assertThat(instance1).isSameAs(instance2);
    }

    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    public void springContainer(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        // 조회 1
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        // 조회 2
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);
        // 인스턴스가 같기 때문에 싱글톤으로 작동하는 것을 확인!
        Assertions.assertThat(memberService1).isSameAs(memberService2);
    }
}