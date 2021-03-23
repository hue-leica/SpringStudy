package domain;


import javax.persistence.*;

@Entity
public class Delivery extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) // STRING으로 해야 중간에 다른 상태가 추가되어도 매핑이 된다
    private DeliveryStatus deliveryStatus; // READY , COMP
}
