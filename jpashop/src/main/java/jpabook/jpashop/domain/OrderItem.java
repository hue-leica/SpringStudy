package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

//    protected OrderItem() {
//    }

    // == 생성 메서드 == //
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice); // 이렇게 하는 이유는 할인정책같은게 있어서 가격이 달라질 수 있기 때문
        orderItem.setCount(count);

        item.removeStock(count); // 재고 감소
        return orderItem;
    }

    // == 비즈니스 로직 == //
    /* Item재고를 다시 추가시켜주는 로직 */
    public void cancel() {
        getItem().addStock(count);
    }

    /* 총 가격 계산 */
    public int getTotalPrice() {
        return getOrderPrice()*getCount();
    }
}
