package kyk.SpringFoodWorldProject.web;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.domain.entity.BoardFile;
import kyk.SpringFoodWorldProject.board.repository.BoardFileRepository;
import kyk.SpringFoodWorldProject.board.repository.SpringDataJpaBoardRepository;
import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUploadDto;
import kyk.SpringFoodWorldProject.comment.service.CommentServiceImpl;
import kyk.SpringFoodWorldProject.follow.domain.entity.Follow;
import kyk.SpringFoodWorldProject.follow.repository.FollowRepository;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.domain.entity.ProfileFile;
import kyk.SpringFoodWorldProject.member.repository.ProfileFileRepository;
import kyk.SpringFoodWorldProject.member.repository.SpringDataJpaMemberRepository;
import kyk.SpringFoodWorldProject.menu.domain.dto.MenuRecommendUploadForm;
import kyk.SpringFoodWorldProject.menu.domain.entity.MenuRecommend;
import kyk.SpringFoodWorldProject.menu.repository.MenuRecommendRepository;
import kyk.SpringFoodWorldProject.menu.service.MenuRecommendServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;

@Profile("local")
@Controller
@RequiredArgsConstructor
public class TestDataInit {
    private final SpringDataJpaMemberRepository memberRepository;
    private final ProfileFileRepository profileFileRepository;
    private final FollowRepository followRepository;
    private final SpringDataJpaBoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final CommentServiceImpl commentService;
    private final MenuRecommendRepository menuRecommendRepository;

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() throws IOException {
        // 회원 데이터 추가 3
        Member savedMember1 = memberRepository.saveMember(new Member("admin", "qq", "qq", "admin"));
        Member savedMember2 = memberRepository.saveMember(new Member("ddd", "dd", "dd", "customer"));
        Member savedMember3 = memberRepository.saveMember(new Member("aaa", "aa", "aa", "customer"));

        profileFileRepository.save(new ProfileFile("user_icon.PNG","user_icon.PNG", savedMember1));
        profileFileRepository.save(new ProfileFile("user_icon.PNG","user_icon.PNG", savedMember2));
        profileFileRepository.save(new ProfileFile("user_icon.PNG","user_icon.PNG", savedMember3));
        followRepository.save(new Follow(savedMember2, savedMember3));

        // 회원, 팔로우 데이터 추가 16
        int memberCount = 1;
        while (memberCount < 17) {
            Member savedMember = memberRepository.saveMember(new Member("테스터1"+memberCount, "a"+memberCount, "a"+memberCount, "customer"));
            profileFileRepository.save(new ProfileFile("user_icon.PNG","user_icon.PNG", savedMember));
            followRepository.save(new Follow(savedMember, savedMember2));
            memberCount++;
        }


        Random random = new Random();
        int lowerBound = 50;
        int upperBound = 1000;

        int lowerBound2 = 100;
        int upperBound2 = 10000;

        Board savedBoard1 = boardRepository.save(new Board("식당추천합니다.dddddddddddddddddddddddddddddddddddd", "내용", "aaa", savedMember3, "자유게시판","사는얘기", null, null, 0, 0));

        // 게시글 데이터 추가
        // 자유게시판 글 및 댓글 19
        int boardCount = 1;
        while (boardCount < 20) {
            int randomLikeCount = lowerBound + random.nextInt(upperBound - lowerBound + 1);
            int randomCount = lowerBound2 + random.nextInt(upperBound2 - lowerBound2 + 1);

            boardRepository.save(new Board("샘플 제목" + (boardCount + 2), "샘플 내용입니다." + boardCount, "ddd", savedMember2, "자유게시판", "사는얘기", null, null, randomCount, randomLikeCount));
            commentService.saveComment(savedMember2.getId(), savedBoard1.getId(), new CommentUploadDto(1L,"안녕하세요"+boardCount));
            boardCount++;
        }

        // 식당추천게시판 글 20
        int boardCount2 = 1;
        while (boardCount2 < 20) {
            int randomLikeCount = lowerBound + random.nextInt(upperBound - lowerBound + 1);
            int randomCount = lowerBound2 + random.nextInt(upperBound2 - lowerBound2 + 1);

            boardRepository.save(new Board("추천 식당 샘플" + (boardCount2+2), "샘플 내용입니다." + boardCount2, "ddd", savedMember2, "추천게시판", "식당", "서울시", null, randomCount, randomLikeCount));
            boardRepository.save(new Board("추천 메뉴 샘플", "내용", "aaa", savedMember3, "추천게시판","메뉴", null, "한식", 0, 0));
            boardCount2++;
        }
        Board savedBoard = boardRepository.save(new Board("식당추천합니다.dddddddddddddddddddddddddddddddddddd", "내용", "aaa", savedMember3, "추천게시판","식당", "부산시", null, 0, 0));


        // 먹스타그램 글 10
        Board savedMuckstar1 = boardRepository.save(new Board("먹스타 샘플 제목1", "예시로 작성한 먹스타그램 게시글입니다", "ddd", savedMember2, "먹스타그램", "말머리 선택", null, null, lowerBound + random.nextInt(upperBound - lowerBound + 1), lowerBound2 + random.nextInt(upperBound2 - lowerBound2 + 1)));
        boardFileRepository.save(new BoardFile("example_muckstar1.jpg", savedMuckstar1));

        Board savedMuckstar2 = boardRepository.save(new Board("먹스타 샘플 제목2", "예시로 작성한 먹스타그램 게시글입니다", "ddd", savedMember2, "먹스타그램", "말머리 선택", null, null, lowerBound + random.nextInt(upperBound - lowerBound + 1), lowerBound2 + random.nextInt(upperBound2 - lowerBound2 + 1)));
        boardFileRepository.save(new BoardFile("example_muckstar2.jpg", savedMuckstar2));

        Board savedMuckstar3 = boardRepository.save(new Board("먹스타 샘플 제목3", "예시로 작성한 먹스타그램 게시글입니다", "ddd", savedMember2, "먹스타그램", "말머리 선택", null, null, lowerBound + random.nextInt(upperBound - lowerBound + 1), lowerBound2 + random.nextInt(upperBound2 - lowerBound2 + 1)));
        boardFileRepository.save(new BoardFile("example_muckstar3.jpg", savedMuckstar3));

        Board savedMuckstar4 = boardRepository.save(new Board("먹스타 샘플 제목4", "예시로 작성한 먹스타그램 게시글입니다", "ddd", savedMember2, "먹스타그램", "말머리 선택", null, null, lowerBound + random.nextInt(upperBound - lowerBound + 1), lowerBound2 + random.nextInt(upperBound2 - lowerBound2 + 1)));
        boardFileRepository.save(new BoardFile("example_muckstar4.jpg", savedMuckstar4));

        Board savedMuckstar5 = boardRepository.save(new Board("먹스타 샘플 제목5", "예시로 작성한 먹스타그램 게시글입니다", "ddd", savedMember2, "먹스타그램", "말머리 선택", null, null, lowerBound + random.nextInt(upperBound - lowerBound + 1), lowerBound2 + random.nextInt(upperBound2 - lowerBound2 + 1)));
        boardFileRepository.save(new BoardFile("example_muckstar5.jpg", savedMuckstar5));

        Board savedMuckstar6 = boardRepository.save(new Board("먹스타 샘플 제목6", "예시로 작성한 먹스타그램 게시글입니다", "ddd", savedMember2, "먹스타그램", "말머리 선택", null, null, lowerBound + random.nextInt(upperBound - lowerBound + 1), lowerBound2 + random.nextInt(upperBound2 - lowerBound2 + 1)));
        boardFileRepository.save(new BoardFile("example_muckstar6.jpg", savedMuckstar6));

        Board savedMuckstar7 = boardRepository.save(new Board("먹스타 샘플 제목7", "예시로 작성한 먹스타그램 게시글입니다", "ddd", savedMember2, "먹스타그램", "말머리 선택", null, null, lowerBound + random.nextInt(upperBound - lowerBound + 1), lowerBound2 + random.nextInt(upperBound2 - lowerBound2 + 1)));
        boardFileRepository.save(new BoardFile("example_muckstar7.jpg", savedMuckstar7));

        Board savedMuckstar8 = boardRepository.save(new Board("먹스타 샘플 제목8", "예시로 작성한 먹스타그램 게시글입니다", "ddd", savedMember2, "먹스타그램", "말머리 선택", null, null, lowerBound + random.nextInt(upperBound - lowerBound + 1), lowerBound2 + random.nextInt(upperBound2 - lowerBound2 + 1)));
        boardFileRepository.save(new BoardFile("example_muckstar8.jpg", savedMuckstar8));

        Board savedMuckstar9 = boardRepository.save(new Board("먹스타 샘플 제목9", "예시로 작성한 먹스타그램 게시글입니다", "ddd", savedMember2, "먹스타그램", "말머리 선택", null, null, lowerBound + random.nextInt(upperBound - lowerBound + 1), lowerBound2 + random.nextInt(upperBound2 - lowerBound2 + 1)));
        boardFileRepository.save(new BoardFile("example_muckstar9.jpg", savedMuckstar9));

        Board savedMuckstar10 = boardRepository.save(new Board("먹스타 샘플 제목10", "예시로 작성한 먹스타그램 게시글입니다", "ddd", savedMember2, "먹스타그램", "말머리 선택", null, null, lowerBound + random.nextInt(upperBound - lowerBound + 1), lowerBound2 + random.nextInt(upperBound2 - lowerBound2 + 1)));
        boardFileRepository.save(new BoardFile("example_muckstar10.jpg", savedMuckstar10));


        // 댓글 데이터 추가 3
        commentService.saveComment(savedMember2.getId(), savedBoard.getId(), new CommentUploadDto(1L,"안녕하세요1"));
        commentService.saveComment(savedMember3.getId(), savedBoard.getId(), new CommentUploadDto(2L,"안녕하세요2"));
        commentService.saveComment(savedMember2.getId(), savedMuckstar1.getId(), new CommentUploadDto(1L,"안녕하세요3"));


        // 메뉴 데이터 추가 18
        menuRecommendRepository.save(new MenuRecommend("기타", "철수네직화곱창", "곱창/대창/막창 세트1", savedMember3, "example_곱창.PNG", "example_곱창.PNG"));
        menuRecommendRepository.save(new MenuRecommend("분식", "김밥천국", "참치김밥/떡볶이 세트", savedMember3, "example_김밥천국_김밥.PNG", "example_김밥천국_김밥.PNG"));
        menuRecommendRepository.save(new MenuRecommend("기타", "맛존매콤닭갈비", "매콤닭갈비", savedMember3, "example_닭갈비.PNG", "example_닭갈비.PNG"));
        menuRecommendRepository.save(new MenuRecommend("기타", "이순신국밥", "돼지국밥", savedMember3, "example_돼지국밥.PNG", "example_돼지국밥.PNG"));
        menuRecommendRepository.save(new MenuRecommend( "분식", "떡볶이주는판다", "떡볶이", savedMember3, "example_분식_떡볶이.PNG", "example_분식_떡볶이.PNG"));
        menuRecommendRepository.save(new MenuRecommend( "샌드위치", "웰빙샌드위치", "웰빙샌드위치", savedMember3, "example_샌드위치.PNG", "example_샌드위치.PNG"));
        menuRecommendRepository.save(new MenuRecommend( "햄버거", "맘스터치", "싸이버거", savedMember3, "example_싸이버거.PNG", "example_싸이버거.PNG"));
        menuRecommendRepository.save(new MenuRecommend( "치킨", "에꿍이치킨", "양념반후라이드반", savedMember3, "example_양념반후라이드반.PNG", "example_양념반후라이드반.PNG"));
        menuRecommendRepository.save(new MenuRecommend( "족발/보쌈", "김삿갓족발보쌈", "족발", savedMember3, "example_족발.PNG", "example_족발.PNG"));
        menuRecommendRepository.save(new MenuRecommend( "중식", "왕비성", "짜장면/샤오롱", savedMember3, "example_중식_짜장면.PNG", "example_중식_짜장면.PNG"));
        menuRecommendRepository.save(new MenuRecommend( "회/초밥", "상무초밥", "기본세트1", savedMember3, "example_초밥.PNG", "example_초밥.PNG"));
        menuRecommendRepository.save(new MenuRecommend( "피자", "도미노피자", "스테이크피자", savedMember3, "example_피자.PNG", "example_피자.PNG"));
        menuRecommendRepository.save(new MenuRecommend( "한식", "백반잘하는집", "김치찌개/계란말이", savedMember3, "example_한식_김치찌개.PNG", "example_한식_김치찌개.PNG"));
        menuRecommendRepository.save(new MenuRecommend( "찜/탕", "조마루감자탕", "감자탕", savedMember3, "example_감자탕.JPG", "example_감자탕.JPG"));
        menuRecommendRepository.save(new MenuRecommend( "일식/돈까스", "너무커서놀란돈까스&별리달리", "치즈돈까스", savedMember3, "example_돈까스.PNG", "example_돈까스.PNG"));
        menuRecommendRepository.save(new MenuRecommend( "샐러드", "리퍼브14샐러드", "샐러드", savedMember3, "example_샐러드.JPG", "example_샐러드.JPG"));
        menuRecommendRepository.save(new MenuRecommend( "도시락/죽", "본죽", "소고기버섯죽", savedMember3, "example_소고기버섯죽.JPG", "example_소고기버섯죽.JPG"));
        menuRecommendRepository.save(new MenuRecommend( "아시안", "천라쿵푸마라탕&마라샹궈", "마라탕", savedMember3, "example_마라탕.JPG", "example_마라탕.JPG"));




    }

}
