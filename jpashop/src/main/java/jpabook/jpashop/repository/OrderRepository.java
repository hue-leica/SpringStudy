package jpabook.jpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.Order;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    @PersistenceContext
    EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    /* Query DSL을 사용한 검색 */
    public List<Order> findAllByQueryDsl(OrderSearch orderSearch){
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        JPAQueryFactory query = new JPAQueryFactory(em);
        return query
                .select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName())) // 값을 넘겨서 없으면 널을 반환
                .limit(1000)
                .fetch();
    }

    private BooleanExpression statusEq(OrderStatus statusCond){
        if(statusCond == null){
            return null;
        }
        return QOrder.order.status.eq(statusCond);
    }

    private BooleanExpression nameLike(String memberName){
        if(!StringUtils.hasText(memberName)){
            return null;
        }
        return QMember.member.name.like(memberName);
    }


    /* 검색 기능 */
    public List<Order> findAllByCriteria(OrderSearch orderSearch){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();

        /* 주문 상태 동적 처리 */
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        /* 회원 이름 동적 처리 */
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        /* 결과적으로 코드는 줄었으나 무슨 쿼리가 나갈지 도무지 생각이 안됨
            --> 표준스펙이긴 하나 실무에서는 사용할 수 X */
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

    /* fetch join 코드 */
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class)
                .getResultList();
    }

    /* XToOne 관계는 fetch join을 사용으로 최적화 + 컬렉션은 hibernate batch size 옵션으로 쿼리 줄이기 */
    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery("select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }


    /* new 패키지명.dto(필드 하나씩 나열 필요)
    *  --> 기본적으로 Entity자체로 인식하지 않기때문에 컬럼을 모두 나열해주고 생성자를 맞춰주어야함 */
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery("select new jpabook.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d",OrderSimpleQueryDto.class)
                .getResultList();
    }

    /* 1:N관계인 테이블에서 join을 하면 당연하게 data수가 뻥튀기가 되기 때문에 distinct 처리를 반드시 해줘야 한다 */
    public List<Order> findAllWithItem() {
        // fetch join은 사실 연관된 테이블과 inner join을 편하게 하고, select에 모두 추가하는 것을 해주는 역할임
        // JPA의 distinct는 객체를 대상으로 중복 제거를 하는 추가적인 기능이 있다
        return em.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i", Order.class)
                .getResultList();
    }
}
