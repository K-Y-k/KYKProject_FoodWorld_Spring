package kyk.SpringFoodWorldProject.comment.service;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateDto;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.repository.BoardRepository;
import kyk.SpringFoodWorldProject.comment.domain.dto.CommentDto;
import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUpdateDto;
import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;
import kyk.SpringFoodWorldProject.comment.repository.CommentRepository;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Override
    public Comment save(Comment comment) {
       return commentRepository.save(comment);
    }

    public Long saveComment(Long memberId, Long boardId, CommentDto dto) {
        Member memberEntity = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + boardId));
        Board boardEntity = boardRepository.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + boardId));

        Comment comment = dto.toEntity(memberEntity, boardEntity);
        commentRepository.save(comment);

        return comment.getId();
    }

    @Override
    public List<Comment> pageList(Pageable pageable) {
        return commentRepository.pageList(pageable);
    }

    @Override
    public void updateComment(Long commentId, CommentUpdateDto updateParam) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow();
        findComment.updateComment(updateParam.getContent());

        log.info("댓글 수정완료");
    }

    @Override
    public void delete(Long commentId) {
        commentRepository.delete(commentId);
    }
}
