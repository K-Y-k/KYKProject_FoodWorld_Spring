package kyk.SpringFoodWorldProject.comment.controller;

import kyk.SpringFoodWorldProject.comment.domain.dto.CommentDto;
import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUpdateDto;
import kyk.SpringFoodWorldProject.comment.service.CommentServiceImpl;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
@ResponseBody
public class CommentController {

    private final CommentServiceImpl commentService;


    @PostMapping("/posts/{id}/comments")
    public ResponseEntity commentSave(@PathVariable Long id,
                                      @RequestBody CommentDto dto,
                                      @SessionAttribute("loginMember") Member member) {
        return ResponseEntity.ok(commentService.commentSave(member.getName(), id, dto));
    }


}
