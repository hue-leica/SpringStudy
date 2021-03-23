package basic.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
// @Component 를 가진 모든 객체를 찾아서 스프링 빈으로 등록!
@ComponentScan (
        // 우리가 수동으로 등록했던능 AppConfig.java의 @Configuration을 제외시키기 위한 필터 기능
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes=Configuration.class)
)
public class AutoAppConfig {

}
