package kyk.SpringFoodWorldProject.board.service;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUploadForm;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateForm;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.domain.entity.BoardFile;
import kyk.SpringFoodWorldProject.board.repository.BoardFileRepository;
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
import org.springframework.data.domain.*;
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
    @Autowired BoardFileRepository boardFileRepository;
    @Autowired SpringDataJpaBoardRepository boardRepository;
    @Autowired LikeServiceImpl likeService;
    @Autowired CommentServiceImpl commentService;

    @Autowired EntityManager entityManager;


    @BeforeEach
    public void init() {
        // ?????? ????????? ?????? 3
        memberRepository.save(new Member("?????????1", "test", "test!"));
        Member savedMember1 = memberRepository.save(new Member("ddd", "dd", "dd"));
        Member savedMember2 = memberRepository.save(new Member("aaa", "aa", "aa"));


        // ????????? ????????? ?????? 20
        int boardCount = 1;
        while (boardCount < 20) {
            boardRepository.save(new Board("??????" + (boardCount+2), "??????" + boardCount, "??????" + (boardCount+1), savedMember1, "???????????????", "????????????"));
            boardCount++;
        }
        Board savedBoard = boardRepository.save(new Board("??????ddddddcddddddddddddddddd????????????????????????????????????ddd", "??????", "??????10", savedMember2, "???????????????","??????"));

    }

    @AfterEach
    public void clear() throws SQLException {
//        this.entityManager
//                .createNativeQuery("ALTER TABLE member auto_increment=1")
//                .executeUpdate();
    }

    /**
     * ??? ?????? ?????????
     */
    @Test
//    @Rollback(value = false)
    void upload() throws Exception {
        // given
        Member member1 = new Member("??????1", "loginId", "pw1");
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

        BoardUploadForm boardDto = new BoardUploadForm(27L, "????????? ??????", "????????? ??????", "???????????????", "????????????", Collections.singletonList(imageFile), Collections.singletonList(attachFile), Collections.singletonList(imageFile.getOriginalFilename()), Collections.singletonList(""), 0);


        // when : Dto??? id??? ????????? db??? ???????????? ?????? ???????????? ???????????? ????????? ???
        Long uploadBoardId = boardService.upload(savedMember.getId(), boardDto);


        // then : ????????? ???????????? ??????
        Board findUploadBoard = boardService.findById(uploadBoardId).get();

        assertThat(findUploadBoard.getTitle()).isEqualTo(boardDto.getTitle());
        assertThat(findUploadBoard.getContent()).isEqualTo(boardDto.getContent());
    }


    /**
     * ??? ?????? ?????????
     */
    @Test
    void updateBoard() {
        // given
        BoardUpdateForm updateDto = new BoardUpdateForm(1L, "????????? ??????", "????????? ??????", "???????????????", "????????????");

        // when : ????????? ????????? ????????? ?????? id??? ?????? ??????
        Long updateBoardId = boardService.updateBoard(updateDto.getId(), updateDto);
        Board updateBoard = boardService.findById(updateBoardId).get();

        // then
        assertThat(updateBoard.getTitle()).isEqualTo(updateDto.getTitle());
        assertThat(updateBoard.getContent()).isEqualTo(updateDto.getContent());
    }


    /**
     * ?????? ?????? ?????????
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
     * ????????? ?????? ?????????
     */
    @Test
    void pageList() {
        // given : freeBoard????????? ??? 19??? / recommend????????? ??? 1???
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        String boardType = "???????????????";

        // when
        Page<Board> result = boardService.findPageListByBoardType(pageable, boardType);

        // then
        Assertions.assertEquals(result.getSize(), 10, "??? ???????????? ???????????? ??????");
        Assertions.assertEquals(result.getTotalPages(), 2, "??? ????????? ??????");
        Assertions.assertEquals(result.getTotalElements(), 19, "????????? ????????? ?????? ?????? ????????? ??????");
        System.out.println("NEXT: " + result.nextPageable());
    }


    /**
     * ?????? ?????? ?????????
     */
    @Test
    void findByTitleContaining() {
        // given : freeBoard????????? ??? 19??? / recommend????????? ??? 1???
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        String titleKeyword = "???";
        String boardType = "???????????????";

        // when
        Page<Board> result = boardService.findByTitleContaining(titleKeyword, pageable, boardType);

        // then
        Assertions.assertEquals(result.getTotalPages(), 2, "??? ????????? ??????");
        Assertions.assertEquals(result.getTotalElements(), 19, "????????? ????????? ?????? ?????? ????????? ??????");
    }

    /**
     * ????????? ?????? ?????????
     */
    @Test
    void findByWriterContaining() {
        // given : freeBoard????????? ??? 19??? / recommend????????? ??? 1???
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        String writerKeyword = "???";
        String boardType = "???????????????";

        // when
        Page<Board> result = boardService.findByWriterContaining(writerKeyword, pageable, boardType);

        // then
        Assertions.assertEquals(result.getTotalPages(), 2, "??? ????????? ??????");
        Assertions.assertEquals(result.getTotalElements(), 19, "????????? ????????? ?????? ?????? ????????? ??????");
    }

    /**
     * ???????????? ?????? ?????? ?????? ?????????
     */
    @Test
    void findByTitleContainingAndWriterContaining() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        String titleKeyword = "??????10";
        String writerKeyword = "???";
        String boardType = "???????????????";

        // when
        Page<Board> result = boardService.findByTitleContainingAndWriterContaining(titleKeyword, writerKeyword, pageable, boardType);

        // then
        Assertions.assertEquals(result.getTotalElements(), 1, "????????? ????????? ?????? ?????? ????????? ??????");
    }


    /**
     * ??? ?????? ?????????
     */
    @Test
    void delete() throws IOException {
        // given
        Member member1 = new Member("??????1", "loginId", "pw1");
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

        BoardUploadForm boardDto = new BoardUploadForm(27L, "????????? ??????", "????????? ??????", "???????????????", "????????????", Collections.singletonList(imageFile), Collections.singletonList(attachFile), Collections.singletonList(imageFile.getOriginalFilename()), Collections.singletonList(""), 0);
        Long uploadBoardId = boardService.upload(savedMember.getId(), boardDto);

        // when
        boardService.delete(uploadBoardId);

        // then
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            Board findBoard = boardService.findById(uploadBoardId).orElseThrow();
        });
//        fail("?????? ????????? ??????.");

    }


    /**
     * ????????? ?????? ?????????
     */
    @Test
    void updateCount() throws IOException {
        // given
        Member member1 = new Member("??????1", "loginId", "pw1");
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

        BoardUploadForm boardDto = new BoardUploadForm(27L, "????????? ??????", "????????? ??????", "???????????????", "????????????", Collections.singletonList(imageFile), Collections.singletonList(attachFile), Collections.singletonList(imageFile.getOriginalFilename()), Collections.singletonList(""), 0);
        Long uploadBoardId = boardService.upload(savedMember.getId(), boardDto);

        // when
        int count = boardService.updateCount(uploadBoardId);

        // then
        assertThat(count).isEqualTo(1);
    }


    /**
     * ????????? ?????? ?????????
     */
    @Test
    void like() throws IOException {
        // given
        Member member1 = new Member("??????1", "loginId", "pw1");
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

        BoardUploadForm boardDto = new BoardUploadForm(27L, "????????? ??????", "????????? ??????", "???????????????", "????????????", Collections.singletonList(imageFile), Collections.singletonList(attachFile), Collections.singletonList(imageFile.getOriginalFilename()), Collections.singletonList(""), 0);
        Long uploadBoardId = boardService.upload(savedMember.getId(), boardDto);

        // when1 : ?????? ???????????? ???????????? ???????????? ????????? ????????? ???
        likeService.saveLike(savedMember.getId(), uploadBoardId);
        int likeCount = likeService.countByBoard_Id(uploadBoardId);

        // then1 : +1??? ?????????
        assertThat(likeCount).isEqualTo(1);


        // when2 : ?????? ?????? ???????????? ????????? ???????????? ????????? ????????? ???
        likeService.saveLike(savedMember.getId(), uploadBoardId);
        int likeCount2 = likeService.countByBoard_Id(uploadBoardId);

        // then2 : -1??? ?????????
        assertThat(likeCount2).isEqualTo(0);
    }


    /**
     * ?????? ?????? ??????
     */
    @Test
    void comment() throws IOException {
        // given
        Member member1 = new Member("??????1", "loginId", "pw1");
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

        BoardUploadForm boardDto = new BoardUploadForm(29L, "????????? ??????", "????????? ??????", "???????????????", "????????????", Collections.singletonList(imageFile), Collections.singletonList(attachFile), Collections.singletonList(imageFile.getOriginalFilename()), Collections.singletonList(""), 0);
        Long uploadBoardId = boardService.upload(savedMember.getId(), boardDto);

        CommentUploadDto commentDto = new CommentUploadDto(1L, "??????????????? ??????");

        // when
        Long savedComment = commentService.saveComment(savedMember.getId(), uploadBoardId, commentDto);

        // then
        assertThat(savedComment).isEqualTo(commentDto.getId());
    }


    /**
     * ?????? ?????? ?????? ??????
     */
    @Test
    void commentCount() throws IOException {
        // given
        Member member1 = new Member("??????1", "loginId", "pw1");
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

        BoardUploadForm boardDto = new BoardUploadForm(29L, "????????? ??????", "????????? ??????", "???????????????", "????????????", Collections.singletonList(imageFile), Collections.singletonList(attachFile), Collections.singletonList(imageFile.getOriginalFilename()), Collections.singletonList(""), 0);
        Long uploadBoardId = boardService.upload(savedMember.getId(), boardDto);

        CommentUploadDto commentDto = new CommentUploadDto(1L, "??????????????? ??????");
        Long savedComment = commentService.saveComment(savedMember.getId(), uploadBoardId, commentDto);

        Board findBoard = boardRepository.findById(uploadBoardId).orElseThrow();


        // when
        int commentCount = commentService.findCommentCount(findBoard.getId());


        // then
        assertThat(commentCount).isEqualTo(1);
    }


    @Test
    @DisplayName("No-Offset ????????? ???????????? lastStoreId??? -1 ?????? page size ?????? ?????????")
    void test() {
        // given
        Slice<Board> boards = boardRepository.searchBySlice( "", 10L, true,
                new BoardSearchCond(),
                PageRequest.ofSize(3), "???????????????");
        try {
            // when
            Long first = boards.getContent().get(0).getId();
            Long last  = boards.getContent().get(2).getId();


            // then
            assertThat(first).isEqualTo(9);
            assertThat(last).isEqualTo(7);
        } catch (IndexOutOfBoundsException e) {
            fail("IndexOutOfBoundsException ?????? ??????");
        }
    }

    @Test
    @DisplayName("????????? ?????????????????? isLast??? true, ???????????? ????????? isLast??? false")
    void checkLast() {
        // given
        Slice<Board> getLastPage = boardRepository.searchBySlice("", 10L, true,
                new BoardSearchCond(),
                PageRequest.ofSize(9), "???????????????");

        Slice<Board> getMiddlePage = boardRepository.searchBySlice("", 10L, true,
                new BoardSearchCond(),
                PageRequest.ofSize(4), "???????????????");

        // when
        boolean isLastPage = getLastPage.isLast();
        boolean isNotLastPage = getMiddlePage.isLast();

        // then
        assertThat(isLastPage).isTrue();
        assertThat(isNotLastPage).isFalse();
    }
}