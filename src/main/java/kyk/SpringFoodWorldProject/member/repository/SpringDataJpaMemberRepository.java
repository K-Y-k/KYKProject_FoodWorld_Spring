package kyk.SpringFoodWorldProject.member.repository;

import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.domain.entity.ProfileFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

/**
 * 스프링 데이터 JPA로 구현한 회원 리포지토리
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SpringDataJpaMemberRepository implements MemberRepository {
    private final JPAMemberRepository memberRepository;
    private final ProfileFileRepository profileFileRepository;

    @Override
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public Member findByName(String name) {
        return memberRepository.findByName(name);
    }


    /**
     * 로그인 id로 상세조회
     */
    // 못 찾을 수 있기에 Optional로 감싸준다.
    public Optional<Member> findByLoginId(String loginId) {
        // 람다를 사용해서 위 코드를 축약한 것 (최근의 기본 형태)
        // 리스트를 stream으로 바꾸고 루프를 돌고 필터의 조건에 만족할 경우에만 다음 단계로 넘어간다.
        // 여기서 다음 단계는 findFirst(처음에 나온 것을 반환)이다.
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }


    public void clear() {
        memberRepository.deleteAll();
    }



    @Override
    public ProfileFile saveProfile(ProfileFile profileFile) {
        return profileFileRepository.save(profileFile);
    }

    @Override
    public ProfileFile findProfileByMember(Member member) {
        return profileFileRepository.findProfileByMember(member);
    }

    @Override
    public void updateProfileImage(String originalFileName, String storedFileName, Long memberId) {
        profileFileRepository.updateProfileImage(originalFileName, storedFileName, memberId);
    }

    @Override
    public Page<Member> findPageBy(Pageable pageable) {
        return memberRepository.findPageBy(pageable);
    }

    @Override
    public Page<Member> findByNameContaining(String memberSearchKeyword, Pageable pageable) {
        return memberRepository.findByNameContaining(memberSearchKeyword, pageable);
    }

    @Override
    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }

}
