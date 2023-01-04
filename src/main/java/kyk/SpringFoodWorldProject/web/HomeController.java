package kyk.SpringFoodWorldProject.web;

import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@ModelAttribute("member") Member member) {
        return "main";
    }

}
