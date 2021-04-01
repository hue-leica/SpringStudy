package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문 상품 생성 - 생성 메소드를 쓰려면 직접 생성해서 쓰지 못하게 생성자를 protected로 막아두자!
        // 위처럼 생성자를 protected로 막아두는 것을 롬복에서 @NoArgsConstructor(access = AccessLevel.PROTECTED)로 하면 됨
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem); // orderItem은 여러개 들어가도록 설계는 해놓음

        // 주문 저장 - order만 save해도 내부에 orderitems나 delivery는 cascade되어있어서 알아서 처리됨
        // cascade 사용시에는 참조를 여러군데서 하는 경우 사용이 위험하다! 즉 이럴때에는 연관관계 편의 메소드로 따로 persist해주자
        orderRepository.save(order);
        return order.getId();
    }

    // 취소
    @Transactional
    public void cancelOrder(Long orderId){
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        // 주문 취소
        // 만약 jpa가 아니였다면 수정한 내용에 대한 쿼리를 날려야한다
        // --> 하지만 jpa를 쓰면 더티채킹 덕분에 알아서 Update query를 날린다! jpa짱!
        order.cancel();
    }

    /* 검색 기능 */
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByQueryDsl(orderSearch);
    }
}
