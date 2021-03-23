package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        /* Model객체에 값을 추가해서 화면으로 넘기는 것 */
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    /* @Valid 를 쓰면 Form에 @NotEmpty처럼 필드를 검증하는 어노테이션을 수행한다 */
    /* @Valid 다음에 BindingResult를 써서 오류가 발생했을 때 예외처리 가능! */
    /* Form을 받을 때 Entity를 쓰는 것은 좋지 않음 --> 검증 및 로직이 동일하지 않을 수도 있기 때문
      --> 결과적으로 Form객체 혹은 DTO사용을 권장! */
    public String create(@Valid MemberForm form, BindingResult result){

        if(result.hasErrors()){ // error가 있으면 if문 처리! 참고로 thymeleaf에서 에러와 form정보를 뷰까지 끌고가서 경고창 띄울 수 있음
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        /* 원래는 이렇게 list를 바로 뿌리지 않고 딱 필요한 데이터만 받는 DTO를 만들어서 화면에 넘겨야함
           --> 지금은 거를 데이터가 없으니 Entity를 넘기는 것이다!
            --> 추가적으로 외부 api와 연결시 Entity를 API에 반환하는 것은 매우매우 위험하다!! 실무에서 사용 X
                (템플릿 엔진 정도야 ServerSide에서 돌기 때문에 괜찮음)*/
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        return "members/memberList";
    }
}
