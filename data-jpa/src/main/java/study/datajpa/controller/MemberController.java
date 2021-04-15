package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Member member){
        /* 넘어온 id가 바로 member에 매칭되고 내부적으로 조회를 자동으로 처리 */
        return member.getUsername();
    }


    @GetMapping("/members")
    public Page<MemberDto> list(@Qualifier("member") Pageable memberPageable,
                                @Qualifier("order") Pageable orderPageable){
        Page<Member> page = memberRepository.findAll(memberPageable);
        Page<MemberDto> result = page.map(m -> new MemberDto(m));
        return result;
    }
}
