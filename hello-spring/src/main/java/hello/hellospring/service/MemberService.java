package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//@Service // 서비스 어노테이션이 없으면 Autowired가 되어도 스프링이 가져오지 못함;
@Transactional // JPA 사용시 Service에 Transactional 어노테이션을 해야한다
public class MemberService {

    private final MemberRepository memberRepository ;

    @Autowired // Service를 DI하려면 해당 생성자에 있는 Repository도 필요하므로 역시 DI해줘야함
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /* 회원가입 */
    public Long join(Member member){
        // 중복회원 검사 (반환형이 Optional로 한번 감싸기 때문에 유용한 함수를 쓸 수 있다 (ifPresent 등등))
        validateDuplicatedMember(member);
        memberRepository.save(member);
        return member.getId();
    }
    // 중복 검사 메소
    private void validateDuplicatedMember(Member member) {
        memberRepository.findByName(member.getName()) // 결과에 바로 ifPresent로 검사!
                .ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        });
    }

    /* 전체 회원 조회 */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    /* 회원 1명 조회  */
    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }
}
