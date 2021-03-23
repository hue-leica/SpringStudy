package basic.core.discount;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* 어노테이션을 만든 뒤, 여기에 @Qualifier 추가! */
@Qualifier("mainDiscountPolicy")

public @interface MainDiscountPolicy {

}
