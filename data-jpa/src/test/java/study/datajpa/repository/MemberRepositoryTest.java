package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @Autowired
    EntityManager em;

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
    }

    @Test
    public void 쿼리메소드() throws Exception{
        //given
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> members = memberRepository.findByAgeGreaterThan(15);
        Boolean result = memberRepository.existsByAge(15);
        //List<Member> findmembers = memberRepository.findByUsername("member1");
        List<Member> findmember = memberRepository.findByUsername("member3");

        //then
        Assertions.assertThat(members.size()).isEqualTo(1);
        Assertions.assertThat(result).isEqualTo(false);
        Assertions.assertThat(findmember).isEmpty();
        //Assertions.assertThat(findmembers.size()).isEqualTo(1);
    }

    @Test
    public void Page() throws Exception{
        //given
        Team team = new Team("TeamA");
        teamRepository.save(team);
        memberRepository.save(new Member("member1",10, team));
        memberRepository.save(new Member("member2", 10, team));
        memberRepository.save(new Member("member3", 10, team));
        memberRepository.save(new Member("member4", 10, team));
        memberRepository.save(new Member("member5", 10, team));
        memberRepository.save(new Member("member6", 20, team));

        //when
        int age = 10;
        /* PageRequest는 Pagealbe의 구현체 */
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        /* DTO로 반환 */
        Page<MemberDto> dtoPage = page.map(m -> new MemberDto());

        //then
        List<Member> content = page.getContent(); // 조회된 데이터 내용
        assertThat(content.size()).isEqualTo(3); // 조회된 데이터 수
        //assertThat(page.getTotalElements()).isEqualTo(5); // 전체 데이터 수
        assertThat(page.getNumber()).isEqualTo(0); // 현재 페이지 번호
        //assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 번호
        assertThat(page.isFirst()).isTrue(); // 첫 페이지인지 확인
        assertThat(page.hasNext()).isTrue(); // 다음 페이지가 있는지 확인
    }
    @Test
    public void JpaEventBaseEntity() throws Exception{
        //given
        Member member1 = new Member("member1");
        memberRepository.save(member1); // @PrePersist

        Thread.sleep(100);
        member1.changeUsername("member2");

        em.flush(); // @PreUpdate
        em.clear();

        //when
        Member member = memberRepository.findById(member1.getId()).get();

        //then
        System.out.println("member.getCreatedDate() = " + member.getCreatedDate());
        System.out.println("member.getUpdatedDate() = " + member.getLastModifiedDate());

    }
}
