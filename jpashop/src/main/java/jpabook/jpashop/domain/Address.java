package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
// @Setter는 놓지 않거나 Private로 해야함 --> 불변객체로 써야하기 때문
public class Address {
    private String city;
    private String street;
    private String zipcode;

    /* 값타입은 생성자로 항상 새로 생성하게 하는 것이 좋다
       --> 기본 생성자는 만들되 혹시 모르니 protected로 해두는것이 좋다 */
    protected Address() {
    }
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
