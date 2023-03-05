package kyk.SpringFoodWorldProject.follow.controller;

import kyk.SpringFoodWorldProject.follow.service.FollowServiceImpl;
import kyk.SpringFoodWorldProject.member.domain.LoginSessionConst;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FollowController {

    private final FollowServiceImpl followService;

    @GetMapping("/members/follow/{toMemberId}")
    public String followUpdate(@PathVariable Long toMemberId,
                             @SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        // 세션에 회원 데이터가 없으면 홈 화면으로 이동
        if(loginMember == null) {
            log.info("로그인 상태가 아님");

            model.addAttribute("message", "회원만 팔로우를 할 수 있습니다. 로그인 먼저 해주세요!");
            model.addAttribute("redirectUrl", "/members/login");
            return "messages";
        }

        followService.followAndUnFollow(loginMember.getId(), toMemberId);

        redirectAttributes.addAttribute("toMemberId", toMemberId);

        return "redirect:/members/profile/{toMemberId}";
    }
}
