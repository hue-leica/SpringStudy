package basic.core.autowired;

import basic.core.AutoAppConfig;
import basic.core.discount.DiscountPolicy;
import basic.core.member.Grade;
import basic.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AllBeanTest {
    @Test
    void findAllBean() {
        ApplicationContext ac = new
                AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);
        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(1L, "userA", Grade.VIP);
        /* 2) discountCode로 우리가 지정할 구현체 이름을 명시! */
        int discountPrice = discountService.discount(member, 10000,"fixDiscountPolicy");
        assertThat(discountService).isInstanceOf(DiscountService.class);
        assertThat(discountPrice).isEqualTo(1000);
    }
    static class DiscountService {
        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policies;
        /* 1) 생성자를 통해 DiscountPolicy 타입을 가진 모든 빈을 Map에 저장! */
        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
            this.policyMap = policyMap;
            this.policies = policies;
            System.out.println("policyMap = " + policyMap);
            System.out.println("policies = " + policies);
        }
        /* 3) 여러개의 빈들 중에 사용자가 명시한 discountCode와 일치하는 Type을 Map에서 추출! */
        public int discount(Member member, int price, String discountCode) {
            DiscountPolicy discountPolicy = policyMap.get(discountCode); // 추출!
            System.out.println("discountCode = " + discountCode);
            System.out.println("discountPolicy = " + discountPolicy);
            return discountPolicy.discount(member, price); // 사용자가 원하는 discountCode와 일치하는 할인 수행!
        }
    }
}
