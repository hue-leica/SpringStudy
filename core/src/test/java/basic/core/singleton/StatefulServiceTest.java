package basic.core.singleton;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.swing.plaf.nimbus.State;

import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    public void statefulServiceSingleton() {
        // 스프링 컨테이너를 통해 빈을 뺄 것이기 때문에 default로 싱글톤으로 작동한다
      ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        statefulService1.order("userA", 10000);
        // 스프링이 기본적으로 싱글톤을 지원해서 기존 객체의 값 변경되게 된다.
        statefulService2.order("userB", 20000);

        // 20000이 나온다 ! --> 공유 필드를 건드리는 것은 매우 위험함!
        // 즉, 스프링 빈은 항상 무상태(Stateless)로 설계해야 한다.
        int price = statefulService1.getPrice();
    }
    static class TestConfig{

        @Bean
        public StatefulService statefulService(){
            return new StatefulService();
        }
    }
}