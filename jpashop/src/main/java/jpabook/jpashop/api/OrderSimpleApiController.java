package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.OrderSimpleQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
 * 여기서는 컬렉션이이 아닌 데이터를 조회하는 경우를 다룸
 * (ManyToOne / OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 * */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll(new OrderSearch());
        return all;
    }

    /* Entity가 아니라 DTO를 받아서 반환해야 한다
    *  --> 아직 LAZY 문제는 해결하지 못한다
    *      반복 루프를 돌면서 각 데이터에 대한 member, delivery를 따로 가져와서 쿼리가 데이터 수만큼 나감
    *      --> 이것은 결과적으로 fetch join을 써서 한방쿼리로 해결해야 한다! */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        /* 반복문을 돌면서 데이터의 개수만큼 query가 나간다 --> 비효율적 */
        List<SimpleOrderDto> collect = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }

    /* fetch join을 통해서 order, member, delivery 테이블을 한방의 쿼리로 가져와서 N+1문제를 해결함
    *  하지만, 나에게 필요한 데이터만 주지는 않고 3 테이블의 모든 정보를 주기때문에 V4에서는 DTO자체로 쿼리를 꺼내어 해결할 수 있음 */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> collect = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }

    /* V4는 선택에 따라 하는것이 좋다  */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4(){
        List<OrderSimpleQueryDto> orders = orderRepository.findOrderDtos();
        return orders;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // 이 때 LAZY로 인해 프록시 객체가 초기화 됨
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // 프록시 객체 초기화
        }
    }
}
