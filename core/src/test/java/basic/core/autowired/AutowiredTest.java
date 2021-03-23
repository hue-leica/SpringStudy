package basic.core.autowired;

import basic.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class AutowiredTest {

    @Test
    public void AutowiredOption(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);

    }

    static class TestBean {

        /* Member는 스프링 빈이 아닌 그냥 객체이기 때문에 무조건 없다
           --> 이렇게 없는 상황에서 null을 처리하는 3가지 방법을 다룬다 */
        @Autowired(required = false)
        public void setNoBean1(Member noBean1){
            System.out.println("setNoBean1 = " + noBean1);
        }

        @Autowired
        public void setNoBean2(@Nullable Member member){
            System.out.println("setNoBean2 = " + member);
        }

        @Autowired
        public void setNoBean3(Optional<Member> member){
            System.out.println("setNoBean3 = " + member);
        }
    }
}
