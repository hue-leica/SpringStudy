package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    /* 실제 실무에서는 이런 통합테스트보다 메서드 하나하나를 검증하는 단위테스트가 더 중요함!
       (실제 DB 안붙히고) */
    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = getMember();
        Book book = getBook("JPA BOOK", 10000, 10);

        int orderCount = 2;
        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order findOrder = orderRepository.findOne(orderId);

        /* message, 기대값, 실제값 매칭! */
        Assert.assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, findOrder.getStatus());
        Assert.assertEquals("주문 가격 = 가격 * 수량",10000*orderCount, findOrder.getTotalPrice());
        Assert.assertEquals("주문한 수량 만큼 재고가 줄어야 함", 8, book.getStockQuantity());
    }

    // Extract method -> cmd + option + M
    // parameter match -> cmd + option + P
    private Book getBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member getMember() {
        Member member = new Member();
        member.setName("A");
        member.setAddress(new Address("서울", "강서", "123-123"));
        em.persist(member);
        return member;
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member = getMember();
        Item item = getBook("Book1", 10000, 10);
        int orderCount = 11;

        //when

        //then
        NotEnoughStockException ex = assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), item.getId(), orderCount);
        });
        assertEquals("need more stock", ex.getMessage());
    }

    @Test
    public void 주문취소() throws Exception{
        //given
        Member member = getMember();
        Item item = getBook("Book1", 10000, 10);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order findOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, findOrder.getStatus() ,"주문 취소 상태는 CANCEL");
        assertEquals(10, item.getStockQuantity(), "주문이 취소된 상품의 재고는 증가해야 한다");
    }
}