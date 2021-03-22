package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 스프링을 쓰지 않는 순수 단위 테스트면 없어도 됨
@Transactional // DB에 접근하기 때문에 트랜잭션안에서 수행되어야 한다. --> 테스트코드에 이 어노테이션 작성시 자동 롤백됨
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    /* 회원 가입 테스트 */
    @Test
    // 자동 롤백 되어서 insert query자체를 하지 않음 --> 보기 위해서는 Rollback 을 false로 처리
    //@Rollback(false)
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member); // 아직 db반영 X, 영속성 컨텍스트에 있음

        //then
        Assertions.assertEquals(member, memberRepository.findOne(savedId));
    }
    @Test
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);

        //then
        /* jUnit5에서는 나오는 오류를 검증하는 방법이 4와 다름! */
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertEquals("이미 존재하는 회원입니다.", thrown.getMessage());
    }
}