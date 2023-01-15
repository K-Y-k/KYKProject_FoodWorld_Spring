package kyk.SpringFoodWorldProject.comment.service;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.repository.BoardRepository;
import kyk.SpringFoodWorldProject.comment.domain.dto.CommentDto;
import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUpdateDto;
import kyk.SpringFoodWorldProject.comment.domain.entity.Comments;
import kyk.SpringFoodWorldProject.comment.repository.CommentRepository;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Override
    public Comments save(Comments comment) {
       return commentRepository.save(comment);
    }

    public Long commentSave(String name, Long id, CommentDto dto) {
        Member member = memberRepository.findByName(name);
        Board board = boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + id));

        dto.setMember(member);
        dto.setBoard(board);

        Comments comment = dto.toEntity();
        commentRepository.save(comment);

        return dto.getId();
    }

    @Override
    public List<Comments> pageList(Pageable pageable) {
        return commentRepository.pageList(pageable);
    }

    @Override
    public void update(Long commentId, CommentUpdateDto updateParam) {
        commentRepository.update(commentId, updateParam);
    }

    @Override
    public void delete(Long commentId) {
        commentRepository.delete(commentId);
    }
}
