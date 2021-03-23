import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SingletonWithPrototypeTest1 {
    @Test
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac = new
                AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);
        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(1); /* 여기서 오류가 날 것임!
         --> Singleton내부에 있는 Prototype Bean은 최초 의존관계 주입 시점에 딱 1번만 수행된다
          --> 즉, 부를 때 마다 Prototype Bean 자체가 생성되지는 않기에 값이 2로 나오게 된다. */
    }
    /* Prototype Bean을 가지는 Singleton Bean을 생성 */
    static class ClientBean {
        @Autowired
        /* Provider를 사용해 DL(Dependency Lookup)을 구현 */
        private ObjectProvider<PrototypeBean> prototypeBeanProvider;

        /* Prototype Bean의 count 필드 값을 증가시키는 부분 */
        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
    }
    /* Prototype Scope를 가지는 Bean */
    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;
        public void addCount() {
            count++;
        }
        public int getCount() {
            return count;
        }
        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init " + this);
        }
        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}