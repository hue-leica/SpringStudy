package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 표현 계층에 적합한 형태로 추출하는 경우에 사용하는 특수한 repository라고 생각하면 된다
// 관심사를 분리해서 확실하게 관리하기 위해 따로 JPA에 직접 DTO를 받는 경우 따로 유지하는 것이 좋다
/* 이렇게 repository를 분리할 경우 DTO도 따로 파일로 만들어서 유지해야 한다
  --> 컨트롤러 내부에 만들면 Repository가 Controller를 참조하는 상황이 발생 */

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    /* 1) 컬렉션을 제외한 값을 채우기 위한 쿼리 실행
    *  2) 컬렉션 필드를 채우기 위해 result를 가지고 순회하며 쿼리 수행 --> M+1문제가 발생 */
    public List<OrderQueryDto> findOrderQueryDtos(){
        List<OrderQueryDto> result = findOrders();

        result.forEach(o ->{
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    /* 컬렉션 필드를 채우기 위한 별도의 쿼리를 생성해야 한다 */
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                " from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id = :orderId",OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    /* 컬렉션을 제외한 flat한 데이터들만 먼저 조회해서 값을 넣는다 */
    public List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d",OrderQueryDto.class)
                .getResultList();
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();
        /* orderId값들 List로 추출 */
        List<Long> orderIds = result.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());
        /* orderItems에는 현재 필요한 orderId에 대한 items들이 하나의 리스트에 들어가 있음
         *   --> 이것을 우리는 List<OrderQueryDto> 로 만들어서 set 해야한다 */
        List<OrderItemQueryDto> orderItems = em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        /* orderId에 따라 하나의 List<OrderItemQueryDto>로 group을 만든 뒤 Map으로 만듬 */
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));

        /* orderId에 해당하는 List를 순회하며 set해주는 과정 */
        result.stream()
                .forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    /* 모~든 테이블을 Join해서 받아버리는 것 --> 불필요한 데이터 중복이 발생되며 페이징은 못함 */
    public List<OrderFlatDto> findAllByDto_flat() {
        List<OrderFlatDto> result = em.createQuery(
                "select new " +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d" +
                        " join o.orderItems oi" +
                        " join oi.item i", OrderFlatDto.class)
                .getResultList();
        return result;
    }
}
