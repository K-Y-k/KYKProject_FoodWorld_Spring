package kyk.SpringFoodWorldProject.member.controller;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.service.BoardServiceImpl;
import kyk.SpringFoodWorldProject.follow.service.FollowServiceImpl;
import kyk.SpringFoodWorldProject.member.domain.LoginSessionConst;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberApiController {
}
