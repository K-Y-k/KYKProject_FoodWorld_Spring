package kyk.SpringFoodWorldProject.member.service;

import kyk.SpringFoodWorldProject.member.domain.dto.JoinForm;
import kyk.SpringFoodWorldProject.member.domain.dto.UpdateForm;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.domain.entity.ProfileFile;
import kyk.SpringFoodWorldProject.member.repository.SpringDataJpaMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberServiceImplTest {

    @Autowired MemberServiceImpl memberService;
    @Autowired SpringDataJpaMemberRepository memberRepository;

    @AfterEach
    void afterEach() {
        memberRepository.clear();
    }

    /**
     * 저장, id 조회
     */
    @Test
    void save_findById() {
        // given
        Member member = new Member("memberA", "id1", "paw123", "customer");

        // when
        Member savedMember = memberRepository.saveMember(member);

        // then
        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(savedMember);
    }


    /**
     * 회원 가입
     */
    @Test
    void join() {
        // given
        JoinForm form = new JoinForm("memberA", "id1", "pw123");
        Member member = form.toEntity();

        // when
//        Long savedMemberId = memberService.join(form);
        Member savedMember = memberRepository.saveMember(member);

        // then
        Member findMember = memberService.findById(savedMember.getId()).get();
        assertThat(member.getId()).isEqualTo(findMember.getId());
//        assertThat(memberEntity.getId()).isEqualTo(findMember.getId());
    }


    /**
     * 회원 전체 조회
     */
    @Test
    void findAll() {
        // given
        Member member1 = new Member("memberA", "id1", "pw1", "customer");
        Member member2 = new Member("memberB", "id2", "pw2", "customer");

        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // when
        List<Member> findAllMember = memberRepository.findAll();

        // then
        assertThat(findAllMember.size()).isEqualTo(2);
    }

    /**
     * 로그인
     */
    @Test
    void login() {
        // given
        Member member1 = new Member("memberA", "id1", "pw1", "customer");
        Member savedMember = memberRepository.saveMember(member1);

        // when
        Member loginMember = memberService.login(savedMember.getLoginId(), savedMember.getPassword());

        // then
        assertThat(savedMember).isEqualTo(loginMember);
    }


    /**
     * 프로필 수정
     */
    @Test
    void changeProfile() throws IOException {
        // given
        Member member1 = new Member("memberA", "id1", "pw1", "customer");
        Member savedMember = memberRepository.saveMember(member1);
        memberService.login(savedMember.getLoginId(), savedMember.getPassword());

        UpdateForm form = new UpdateForm(savedMember.getId(), "changeName", "changeId", "changePw", "hello", null);

        // when
        Long updateMemberId = memberService.changeProfile(savedMember.getId(), form);

        // then
        assertThat(updateMemberId).isEqualTo(savedMember.getId());
    }

}