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

        followService.followAndUnFollow(loginMember.getId(), toMemberId);

        redirectAttributes.addAttribute("toMemberId", toMemberId);

        return "redirect:/members/profile/{toMemberId}";
    }
}
