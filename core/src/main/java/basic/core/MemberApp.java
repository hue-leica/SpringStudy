package basic.core;

import basic.core.member.Grade;
import basic.core.member.Member;
import basic.core.member.MemberService;
import basic.core.member.MemberServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {

    /* 이런식으로 Main 메서드를 통한 테스트 코드를 작성할 수 있지만 한계가 있다! 그래서 junit 이용 */
    public static void main(String[] args) {
/*        AppConfig appconfiog = new AppConfig();
        MemberService memberService = appconfiog.memberService();*/

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);// memberService를 가져오려 하는 코드

        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        System.out.println("new Member = "+member);
        System.out.println("find Member = "+findMember);
    }
}
