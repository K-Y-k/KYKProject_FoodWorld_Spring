package kyk.SpringFoodWorldProject.domain.member.controller;

import kyk.SpringFoodWorldProject.domain.member.entity.Member;
import kyk.SpringFoodWorldProject.domain.member.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class RegisterController {

    private final MemberRepository memberRepository;

    /**
     *  회원 등록 폼
     */
    @GetMapping("/member_register")
    public String memberRegisterForm(@ModelAttribute("member") Member member) {
        return "members/member_register";
    }

    /**
     *  회원 저장 기능
     */
    @PostMapping("/member_register")
    public String save(@Valid @ModelAttribute Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/member_register";
        }

        memberRepository.save(member);
        return "redirect:/";
    }

}
