package kyk.SpringFoodWorldProject.menu.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("menu")
@RequiredArgsConstructor
public class MenuController {


    @GetMapping("")
    public String menuRecommend(Model model) {

        return "/menurecommend/menu_recommend";
    }

}
