package kyk.SpringFoodWorldProject.domain.member.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    /**
     * 저장 기능
     */
    public Member save(Member member) {
        member.setId(++sequence);            // id를 1씩 늘려주고
        log.info("save: member={}", member);
        store.put(member.getId(), member);   // 메모리 저장소에 넣어주기
        return member;
    }


    /**
     * 회원 id로 상세조회
     */
    public Member findById(Long id) {
        return store.get(id);
    }


    /**
     * 로그인 id로 상세조회
     */
    // 못 찾을 수 있기에 Optional로 감싸준다.
    public Optional<Member> findByLoginId(String loginId) {
//        List<Member> all = findAll();
//
//        // 모두 조회
//        for (Member m : all) {
//            if (m.getLoginId().equals(loginId)) { // 조회한 것이 받아온 로그인 id와 같다면 반환
//                return Optional.of(m);
//            }
//        }
//        return Optional.empty(); // 없을 경우 빈 값으로 반환

        // 람다를 사용해서 위 코드를 축약한 것 (최근의 기본 형태)
        // 리스트를 stream으로 바꾸고 루프를 돌고 필터의 조건에 만족할 경우에만 다음 단계로 넘어간다.
        // 여기서 다음 단계는 findFirst(처음에 나온 것을 반환)이다.
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }


    /**
     * 회원 모두 조회
     */
    public List<Member> findAll() {
        return new ArrayList<>(store.values()); // store.values()로 저장소의 값들이 리스트 타입로 변환되어 반환
    }


    /**
     * 저장소 초기화
     */
    public void clearStore() {
        store.clear();
    }
}
