package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    // MemoryMemberRepository를 서비스에서 생성하지 않고 테스트 코드 시작 전에 생성해서 전달! (DI)
    @BeforeEach
    public void beforeEach(){
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach(){
        memberRepository.clearStore();
    }

    // Test는 한글로 바꾸어도 좋다! 우리가 보기 좋기 때문
    // 보통 테스트 코드는 given -> when -> then을 따르게 되니까 주석으로 표시도 많이함!
    @Test
    void 회원가입() {
        //given - 이런 상황이 주어지면
        Member member = new Member();
        member.setName("spring");
        
        //when - 실행했을 때
        Long saveId = memberService.join(member);
        
        //then - 결과가 나와야 한다
        Member findMember = memberService.findOne(saveId).get();
        Assertions.assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    void 중복_회원_예외(){
        // given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        // when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다");

        // then
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}