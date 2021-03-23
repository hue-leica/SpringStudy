package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

// Controller라는 어노테이션이 있으면 스프링 컨테이너가 생성하고 관리를 한다!
@Controller
public class MemberController {
     private MemberService memberService;
    // @Autowired MemberService memberService; 이 방법은 의존성 주입 방법중 2) 필드 주입 이다 -> 중간에 건드릴 수 없어서 별로

/*    @Autowired // 3) setter 의존성 주입 방법 -> 조립 시점에 만 변경하는 것이 아닌 수시로 변경 가능해서 별로
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }*/

    // 생성자에 Autowired가 있으면 스프링 컨테이너에서 연결해준다. 이것이 바로 DI 즉, 1) 의존성 주입이다.
    // 이 방법은 생성자를 사용한 의존성 주입 방법이다 --> 이게 제일 괜찮음, 최초 조립시에만 변경하기 때문!
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm(){
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(MemberForm form){
        Member member = new Member();
        member.setName(form.getName());
        memberService.join(member);
        return "redirect:/";
    }
    
    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
