package kyk.SpringFoodWorldProject.board.service;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardUploadForm;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateForm;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.repository.JPABoardRepository;
import kyk.SpringFoodWorldProject.board.repository.SpringDataJpaBoardRepository;
import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUploadDto;
import kyk.SpringFoodWorldProject.comment.repository.JPACommentRepository;
import kyk.SpringFoodWorldProject.comment.service.CommentServiceImpl;
import kyk.SpringFoodWorldProject.like.service.LikeServiceImpl;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.JPAMemberRepository;
import kyk.SpringFoodWorldProject.member.repository.SpringDataJpaMemberRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@Profile("test")
@Transactional
@SpringBootTest
class BoardServiceImplTest {

    @Autowired SpringDataJpaMemberRepository memberRepository;
    @Autowired BoardServiceImpl boardService;
    @Autowired SpringDataJpaBoardRepository boardRepository;
    @Autowired LikeServiceImpl likeService;
    @Autowired CommentServiceImpl commentService;

    @Autowired EntityManager entityManager;


    @BeforeEach
    public void init() {
        // 회원 데이터 추가 3
        memberRepository.save(new Member("테스터1", "test", "test!"));
        Member savedMember1 = memberRepository.save(new Member("ddd", "dd", "dd"));
        Member savedMember2 = memberRepository.save(new Member("aaa", "aa", "aa"));


        // 게시글 데이터 추가 20
        int boardCount = 1;
        while (boardCount < 20) {
            boardRepository.save(new Board("제목" + (boardCount+2), "내용" + boardCount, "작가" + (boardCount+1), savedMember1, "freeBoard", "사는얘기"));
            boardCount++;
        }
        Board savedBoard = boardRepository.save(new Board("제목ddddddcdddddddddddddddddㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇddd", "내용", "작가10", savedMember2, "recommendBoard","식당"));
    }

    @AfterEach
    public void clear() throws SQLException {
//        this.entityManager
//                .createNativeQuery("ALTER TABLE member auto_increment=1")
//                .executeUpdate();
    }

    /**
     * 글 등록 테스트
     */
    @Test
//    @Rollback(value = false)
    void upload() throws Exception {
        // given
        Member member1 = new Member("이름1", "loginId", "pw1");
        Member savedMember = memberRepository.save(member1);

        MockMultipartFile imageFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        MockMultipartFile attachFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        BoardUploadForm boardDto = new BoardUploadForm(27L, "등록한 제목", "등록한 내용", "자유게시판", "사는얘기", Collections.singletonList(imageFile), Collections.singletonList(attachFile), Collections.singletonList(imageFile.getOriginalFilename()), Collections.singletonList(""), 0);


        // when : Dto의 id는 어차피 db에 저장되는 것이 아니므로 아무거나 넣어도 됨
        Long uploadBoardId = boardService.upload(savedMember.getId(), boardDto);


        // then : 등록한 속성들로 비교
        Board findUploadBoard = boardService.findById(uploadBoardId).get();

        assertThat(findUploadBoard.getTitle()).isEqualTo(boardDto.getTitle());
        assertThat(findUploadBoard.getContent()).isEqualTo(boardDto.getContent());
    }


    /**
     * 글 수정 테스트
     */
    @Test
    void updateBoard() {
        // given
        BoardUpdateForm updateDto = new BoardUpdateForm(15L, "수정한 제목", "수정한 내용", "자유게시판", "사는얘기");

        // when : 기존에 생성된 게시글 중의 id를 하나 넣음
        Long updateBoardId = boardService.updateBoard(updateDto.getId(), updateDto);
        Board updateBoard = boardService.findById(updateBoardId).get();

        // then
        assertThat(updateBoard.getTitle()).isEqualTo(updateDto.getTitle());
        assertThat(updateBoard.getContent()).isEqualTo(updateDto.getContent());
    }


    /**
     * 전체 조회 테스트
     */
    @Test
    void findAll() {
        // given
        // when
        List<Board> boards = boardService.findAll();

        // then
        assertThat(boards.size()).isEqualTo(20);
    }


    /**
     * 페이지 처리 테스트
     */
    @Test
    void pageList() {
        // given : freeBoard타입인 글 19개 / recommend타입인 글 1개
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        String boardType = "freeBoard";

        // when
        Page<Board> result = boardService.findPageListByBoardType(pageable, boardType);

        // then
        Assertions.assertEquals(result.getSize(), 10, "한 페이지당 게시글의 개수");
        Assertions.assertEquals(result.getTotalPages(), 2, "총 페이지 개수");
        Assertions.assertEquals(result.getTotalElements(), 19, "페이지 객체에 담긴 모든 게시글 개수");
        System.out.println("NEXT: " + result.nextPageable());
    }


    /**
     * 제목 검색 테스트
     */
    @Test
    void findByTitleContaining() {
        // given : freeBoard타입인 글 19개 / recommend타입인 글 1개
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        String titleKeyword = "제";
        String boardType = "freeBoard";

        // when
        Page<Board> result = boardService.findByTitleContaining(titleKeyword, pageable, boardType);

        // then
        Assertions.assertEquals(result.getTotalPages(), 2, "총 페이지 개수");
        Assertions.assertEquals(result.getTotalElements(), 19, "페이지 객체에 담긴 모든 게시글 개수");
    }

    /**
     * 글쓴이 검색 테스트
     */
    @Test
    void findByWriterContaining() {
        // given : freeBoard타입인 글 19개 / recommend타입인 글 1개
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        String writerKeyword = "작";
        String boardType = "freeBoard";

        // when
        Page<Board> result = boardService.findByWriterContaining(writerKeyword, pageable, boardType);

        // then
        Assertions.assertEquals(result.getTotalPages(), 2, "총 페이지 개수");
        Assertions.assertEquals(result.getTotalElements(), 19, "페이지 객체에 담긴 모든 게시글 개수");
    }

    /**
     * 글쓴이와 제목 동시 검색 테스트
     */
    @Test
    void findByTitleContainingAndWriterContaining() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        String titleKeyword = "제목10";
        String writerKeyword = "작";
        String boardType = "freeBoard";

        // when
        Page<Board> result = boardService.findByTitleContainingAndWriterContaining(titleKeyword, writerKeyword, pageable, boardType);

        // then
        Assertions.assertEquals(result.getTotalElements(), 1, "페이지 객체에 담긴 모든 게시글 개수");
    }


    /**
     * 글 삭제 테스트
     */
    @Test
    void delete() throws IOException {
        // given
        Member member1 = new Member("이름1", "loginId", "pw1");
        Member savedMember = memberRepository.save(member1);

        MockMultipartFile imageFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        MockMultipartFile attachFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        BoardUploadForm boardDto = new BoardUploadForm(27L, "등록한 제목", "등록한 내용", "자유게시판", "사는얘기", Collections.singletonList(imageFile), Collections.singletonList(attachFile), Collections.singletonList(imageFile.getOriginalFilename()), Collections.singletonList(""), 0);
        Long uploadBoardId = boardService.upload(savedMember.getId(), boardDto);

        // when
        boardService.delete(uploadBoardId);

        // then
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            Board findBoard = boardService.findById(uploadBoardId).orElseThrow();
        });
//        fail("글이 없어야 한다.");

    }


    /**
     * 조회수 증가 테스트
     */
    @Test
    void updateCount() throws IOException {
        // given
        Member member1 = new Member("이름1", "loginId", "pw1");
        Member savedMember = memberRepository.save(member1);

        MockMultipartFile imageFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        MockMultipartFile attachFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        BoardUploadForm boardDto = new BoardUploadForm(27L, "등록한 제목", "등록한 내용", "자유게시판", "사는얘기", Collections.singletonList(imageFile), Collections.singletonList(attachFile), Collections.singletonList(imageFile.getOriginalFilename()), Collections.singletonList(""), 0);
        Long uploadBoardId = boardService.upload(savedMember.getId(), boardDto);

        // when
        int count = boardService.updateCount(uploadBoardId);

        // then
        assertThat(count).isEqualTo(1);
    }


    /**
     * 좋아요 적용 테스트
     */
    @Test
    void like() throws IOException {
        // given
        Member member1 = new Member("이름1", "loginId", "pw1");
        Member savedMember = memberRepository.save(member1);

        MockMultipartFile imageFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        MockMultipartFile attachFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        BoardUploadForm boardDto = new BoardUploadForm(27L, "등록한 제목", "등록한 내용", "자유게시판", "사는얘기", Collections.singletonList(imageFile), Collections.singletonList(attachFile), Collections.singletonList(imageFile.getOriginalFilename()), Collections.singletonList(""), 0);
        Long uploadBoardId = boardService.upload(savedMember.getId(), boardDto);

        // when1 : 해당 게시글에 좋아요를 누른적이 없었던 회원일 때
        likeService.saveLike(savedMember.getId(), uploadBoardId);
        int likeCount = likeService.countByBoard_Id(uploadBoardId);

        // then1 : +1이 적용됨
        assertThat(likeCount).isEqualTo(1);


        // when2 : 전에 해당 게시글을 좋아요 누른적이 있었던 회원일 때
        likeService.saveLike(savedMember.getId(), uploadBoardId);
        int likeCount2 = likeService.countByBoard_Id(uploadBoardId);

        // then2 : -1로 취소됨
        assertThat(likeCount2).isEqualTo(0);
    }


    /**
     * 댓글 작성 기능
     */
    @Test
    void comment() throws IOException {
        // given
        Member member1 = new Member("이름1", "loginId", "pw1");
        Member savedMember = memberRepository.save(member1);

        MockMultipartFile imageFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        MockMultipartFile attachFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        BoardUploadForm boardDto = new BoardUploadForm(29L, "등록한 제목", "등록한 내용", "자유게시판", "사는얘기", Collections.singletonList(imageFile), Collections.singletonList(attachFile), Collections.singletonList(imageFile.getOriginalFilename()), Collections.singletonList(""), 0);
        Long uploadBoardId = boardService.upload(savedMember.getId(), boardDto);

        CommentUploadDto commentDto = new CommentUploadDto(1L, "안녕하세요 댓글");

        // when
        Long savedComment = commentService.saveComment(savedMember.getId(), uploadBoardId, commentDto);

        // then
        assertThat(savedComment).isEqualTo(commentDto.getId());
    }


    /**
     * 댓글 개수 출력 기능
     */
    @Test
    void commentCount() throws IOException {
        // given
        Member member1 = new Member("이름1", "loginId", "pw1");
        Member savedMember = memberRepository.save(member1);

        MockMultipartFile imageFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        MockMultipartFile attachFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        BoardUploadForm boardDto = new BoardUploadForm(29L, "등록한 제목", "등록한 내용", "자유게시판", "사는얘기", Collections.singletonList(imageFile), Collections.singletonList(attachFile), Collections.singletonList(imageFile.getOriginalFilename()), Collections.singletonList(""), 0);
        Long uploadBoardId = boardService.upload(savedMember.getId(), boardDto);

        CommentUploadDto commentDto = new CommentUploadDto(1L, "안녕하세요 댓글");
        Long savedComment = commentService.saveComment(savedMember.getId(), uploadBoardId, commentDto);

        Board findBoard = boardRepository.findById(uploadBoardId).orElseThrow();


        // when
        int commentCount = commentService.findCommentCount(findBoard.getId());


        // then
        assertThat(commentCount).isEqualTo(1);
    }

}