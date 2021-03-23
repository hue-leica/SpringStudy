package basic.core.beanfind;

import basic.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextInfoTest {
    
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext((AppConfig.class));
    
    @Test
    @DisplayName("모든 빈 출력하기")
    public void findAllBean(){
        String [] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName);
            System.out.println("name = "+beanDefinitionName + " object = "+bean);
        }
    }

    @Test
    @DisplayName("Application 빈 출력하기")
    public void findApplicationBean(){
        String [] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            /* 빈의 Role이 Application인 것은 바로 우리가 개발하기 위해 직접 등록한 빈을 의미함 */
            /* Role이 INFRASTRUCTURE인 것은 스프링 내부에서 사용하는 빈을 의미 */
            if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION){
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name = "+beanDefinitionName + " object = "+bean);
            }
        }
    }
}
