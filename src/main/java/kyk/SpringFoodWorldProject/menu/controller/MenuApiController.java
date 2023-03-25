package kyk.SpringFoodWorldProject.menu.controller;

import kyk.SpringFoodWorldProject.member.domain.LoginSessionConst;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.service.MemberServiceImpl;
import kyk.SpringFoodWorldProject.menu.domain.entity.MenuRecommend;
import kyk.SpringFoodWorldProject.menu.service.MenuRecommendServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuApiController {
    private final MemberServiceImpl memberService;
    private final MenuRecommendServiceImpl menuRecommendService;

    @GetMapping("/api/menuRecommendList")
    public ResponseEntity<?> menuRecommendList(@SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                               @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        Page<MenuRecommend> menuRecommends = menuRecommendService.PageList(loginMember.getId(), pageable);

        for (MenuRecommend menuRecommend : menuRecommends) {
            log.info("리스트 = {}", menuRecommends.getContent());
        }

        return new ResponseEntity<>(menuRecommends, HttpStatus.OK);
    }
}
