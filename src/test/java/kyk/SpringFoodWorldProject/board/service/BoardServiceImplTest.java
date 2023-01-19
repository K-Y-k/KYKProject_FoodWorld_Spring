package kyk.SpringFoodWorldProject.board.service;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardDto;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateDto;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.comment.domain.dto.CommentDto;
import kyk.SpringFoodWorldProject.comment.service.CommentServiceImpl;
import kyk.SpringFoodWorldProject.like.service.LikeServiceImpl;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.SpringDataJpaMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class BoardServiceImplTest {

    @Autowired SpringDataJpaMemberRepository memberRepository;
    @Autowired BoardServiceImpl boardService;

    @Autowired LikeServiceImpl likeService;

    @Autowired CommentServiceImpl commentService;

    /**
     * 글 등록 테스트
     */
    @Test
//    @Rollback(value = false)
    void upload() {
        // given
        Member member1 = new Member("이름1", "loginId", "pw1");
        Member savedMember = memberRepository.save(member1);

        // when : Dto의 id는 어차피 db에 저장되는 것이 아니므로 아무거나 넣어오 됨
        BoardDto boardDto = new BoardDto(27L, "등록한 제목", "등록한 내용");

        // when
        Long uploadBoardId = boardService.upload(savedMember.getId(), boardDto);

        Board findUploadBoard = boardService.findById(uploadBoardId).get();

        // then : 등록한 속성들로 비교
        assertThat(findUploadBoard.getTitle()).isEqualTo(boardDto.getTitle());
        assertThat(findUploadBoard.getContent()).isEqualTo(boardDto.getContent());
    }


    /**
     * 글 수정 테스트
     */
    @Test
    void updateBoard() {
        // given
        BoardUpdateDto updateDto = new BoardUpdateDto("수정한 제목", "수정한 내용");

        // when : 기존에 생성된 게시글 중의 id를 하나 넣음
        Long updateBoardId = boardService.updateBoard(23L, updateDto);
        Board updateBoard = boardService.findById(updateBoardId).get();

        // then
        assertThat(updateBoardId).isEqualTo(23L);
        assertThat(updateBoard.getTitle()).isEqualTo(updateDto.getTitle());
        assertThat(updateBoard.getContent()).isEqualTo(updateDto.getContent());
    }


    /**
     * 전체 조회 테스트
     */
    @Test
    void findAll() {
        // given : 이미 TestDataInit() 클래스로 글이 19개 생성되어 있음
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
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");

        // when
        Page<Board> result = boardService.pageList(pageable);

        // then
        Assertions.assertEquals(result.getSize(), 10, "한 페이지당 게시글의 개수");
        Assertions.assertEquals(result.getTotalPages(), 2, "총 페이지 개수");
        Assertions.assertEquals(result.getTotalElements(), 20, "페이지 객체에 담긴 모든 게시글 개수");
        System.out.println("NEXT: " + result.nextPageable());
    }


    /**
     * 제목 검색 테스트
     */
    @Test
    void findByTitleContaining() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        String titleKeyword = "제";

        // when
        Page<Board> result = boardService.findByTitleContaining(titleKeyword, pageable);

        // then
        Assertions.assertEquals(result.getTotalPages(), 2, "총 페이지 개수");
        Assertions.assertEquals(result.getTotalElements(), 20, "페이지 객체에 담긴 모든 게시글 개수");
    }

    /**
     * 글쓴이 검색 테스트
     */
    @Test
    void findByWriterContaining() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        String writerKeyword = "작";

        // when
        Page<Board> result = boardService.findByWriterContaining(writerKeyword, pageable);

        // then
        Assertions.assertEquals(result.getTotalPages(), 2, "총 페이지 개수");
        Assertions.assertEquals(result.getTotalElements(), 20, "페이지 객체에 담긴 모든 게시글 개수");
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

        // when
        Page<Board> result = boardService.findByTitleContainingAndWriterContaining(titleKeyword, writerKeyword, pageable);

        // then
        Assertions.assertEquals(result.getTotalElements(), 1, "페이지 객체에 담긴 모든 게시글 개수");
    }


    /**
     * 글 삭제 테스트
     */
    @Test
    void delete() {
        // given
        Member member1 = new Member("이름1", "loginId", "pw1");
        Member savedMember = memberRepository.save(member1);

        BoardDto boardDto = new BoardDto(27L, "등록한 제목", "등록한 내용");
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
    void updateCount() {
        // given
        Member member1 = new Member("이름1", "loginId", "pw1");
        Member savedMember = memberRepository.save(member1);

        BoardDto boardDto = new BoardDto(27L, "등록한 제목", "등록한 내용");
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
    void like() {
        // given
        Member member1 = new Member("이름1", "loginId", "pw1");
        Member savedMember = memberRepository.save(member1);

        BoardDto boardDto = new BoardDto(27L, "등록한 제목", "등록한 내용");
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
    void comment () {
        // given
        Member member1 = new Member("이름1", "loginId", "pw1");
        Member savedMember = memberRepository.save(member1);

        BoardDto boardDto = new BoardDto(27L, "등록한 제목", "등록한 내용");
        Long uploadBoardId = boardService.upload(savedMember.getId(), boardDto);

        CommentDto commentDto = new CommentDto(28L, "안녕하세요 댓글");

        // when
        Long savedComment = commentService.saveComment(savedMember.getId(), uploadBoardId, commentDto);

        // then
        assertThat(savedComment).isEqualTo(commentDto.getId());
    }



}