package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional // DB관련 모든 작업은 트랜잭션 안에서 수행되어야 한다
@RequiredArgsConstructor // 생성자를 통한 의존성 주입을 자동으로 해줌 --> final키워드 붙은 필드를 찾아서 !
                         // 필요에 따라 final안붙은 필드가 있을수도 있기 때문에 @AllArgsConstructor보다 유연함
public class MemberService {

    // 필드 주입 or setter주입 으로 의존성 주입을 할 수 있으나 생성자 주입 + 롬복 조합이 제일 좋다
    // final로 하면 컴파일 시점에 값이 들어가는지 확인할 수 있어서 좋다
    private final MemberRepository memberRepository;

    /* 회원 가입 */
    public Long join(Member member){
        validateDuplicateMember(member); // 중복 아이디 검사
        memberRepository.save(member);
        return member.getId();
    }
    // 회원 아이디 중복 검사
    private void validateDuplicateMember(Member member) {
        /* 동시 같은 이름으로 등록이 오는 경우에 문제가 될 수 있다
            --> DB에 Member.name 컬럼을 유니크 제약조건으로 걸어두면 됨! */
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /* 회원 전체 조회 */
    @Transactional(readOnly = true) // 조회를 하는 기능에는 읽기 옵션을 주면 더티체킹도 안하고 등등 작업이 줄어서 최적화가 된다
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOne(Long id){
        return memberRepository.findOne(id);
    }
}
