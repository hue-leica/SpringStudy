package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

//@Controller @ResponseBody
// @ResponseBody는 데이터를 response body에 데이터를 받아서 보내기 위한 것
@RestController // @Controller + @ResponseBody 를 합친 어노테이션
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    //=================회원 가입======================//
    @PostMapping("/api/v1/members") // @RequestBody를 쓰면 json으로 온 데이터를 Member에알아서 매핑함
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /* v2의 경우 Entity의 필드가 바뀌어도 만약 setter를 닫고 생성자로 만든다면 그대로 유지가 가능함 */
    /* 그리고 DTO를 사용하면 클라이언트가 어디까지 정보를 보내주는지 바로 확인할 수 있다 */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid createMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    //=================회원 수정======================//
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request){
            memberService.update(id, request.getName());
        Member findmember = memberService.findOne(id); // 이렇게 확인을 해서 주는것이 좋음
        return new UpdateMemberResponse(findmember.getId(), findmember.getName());
    }
    //=================전체 회원 조회======================//
    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }

    /* Entity를 직접 반환하지 말고 DTO로 반환해야 한다 */
    @GetMapping("/api/v2/members")
    public Result membersV2(){
        List<Member> members = memberService.findMembers();
        List<MemberDto> collect = members.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private int count;
        private T data;
    }

    @Data
    static class createMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;
        public CreateMemberResponse(Long id) {

            this.id = id;
        }
    }

    @Data
    @AllArgsConstructor // 단순 데이터를 받는 DTO는 그냥 모든 필드를 주입하는 @AllargsConstructor를 사용해도 좋다
    static class UpdateMemberResponse {
        @NotEmpty
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }
}
