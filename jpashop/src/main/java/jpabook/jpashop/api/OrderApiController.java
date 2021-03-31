package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    /* Entity를 직접 반환하기 때문에 json 무한루프가 발생될 것임
      --> DTO를 사용하면 되지만 v1이니까 안씀, Entity에 @JsonIgnore을 써야 해결됨 */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAll(new OrderSearch());
        /* 프록시 객체들을 강제로 초기화 하는 것 */
        for (Order order : all) {
            order.getMember().getName(); // member 테이블
            order.getDelivery().getAddress(); // delivery 테이블
            List<OrderItem> orderItems = order.getOrderItems(); // orderItem 테이블
            /* 람다 형식으로 내부 조회를 깔끔하게 처리 */
            orderItems.stream().forEach(o -> o.getItem().getName()); // item 테이블
        }
        return all;
    }

    /* 반환되는 모든 데이터를 DTO로 해주는 방식
       --> 초기화되지 않은 프록시 객체 문제 & json 무한 루프 문제는 해결됨
       --> N+1문제는 해결되지 않음 */
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    /* Hibernate의 배치 사이즈 옵션 설정해서 SQL에서 IN 키워드를 사용하게 해서 최적화 하는 방법
       --> fetch join까지는 아니더라도 1+N을 1+1로 만들 수 있다 */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit){
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    /* JPA에 직접DTO로 값을 받아서 원하는 필드만 받았지만 N+1 문제가 여전히 발생 */
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDtos();
    }

    /* SQL의 IN키워드를 사용해서 쿼리를 한번에 조회한 후 DTO.set~() 으로 컬렉션값을 지정하는 방식
    *   --> N+1 문제 해결 */
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5(){
        return orderQueryRepository.findAllByDto_optimization();
    }

    /* OrderQueryDto 형식으로 API스펙을 이전과 맞추어 주려면 노가다로 값을 빼고 매칭하면 됨 --> 매우 번거로움
    *  Query는 1개로 줄어들지만, 페이징이 불가능함 */
    @GetMapping("/api/v6/orders")
    public List<OrderFlatDto> ordersV6(){
        return orderQueryRepository.findAllByDto_flat();
    }

    // @Data를 쓰면 Getter toString 등등을 알아서 처리해줌
    @Data
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        // OrderItem도 DTO로 바꿔주어야 한다!
        //private List<OrderItem> orderItems;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            /* 프록시 객체 초기화
               --> 결국 Entity의 모든 필드가 출력되게 된다
                 --> Entity가 바뀌면 API 스펙이 바뀌는 문제가 생긴다! */
            // 내부 참조된 컬렉션 Entity도 역시 DTO로 바꿔주어야 한다
            // 이렇게 안하면 '초기화된 프록시 객체만 반환'하는 'hibernate5Module 설정'까지 해야함 --> 별로임
            orderItems = order.getOrderItems().stream()
                        .map(oi -> new OrderItemDto(oi))
                        .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto{

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem){
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
