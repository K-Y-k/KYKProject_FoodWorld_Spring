package kyk.SpringFoodWorldProject.menu.controller;

import kyk.SpringFoodWorldProject.member.domain.LoginSessionConst;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.service.MemberServiceImpl;
import kyk.SpringFoodWorldProject.menu.domain.dto.MenuRecommendUploadForm;
import kyk.SpringFoodWorldProject.menu.domain.entity.MenuRecommend;
import kyk.SpringFoodWorldProject.menu.service.MenuRecommendServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {

    private final MemberServiceImpl memberService;
    private final MenuRecommendServiceImpl menuRecommendService;


    @GetMapping("")
    public String menuRecommend(@SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                @PageableDefault(page = 0, size = 7, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                Model model) {

        if(loginMember != null) {
            Page<MenuRecommend> menuRecommends = menuRecommendService.PageList(loginMember.getId(), pageable);

            model.addAttribute("menuRecommends", menuRecommends);

            for (MenuRecommend menuRecommend : menuRecommends) {
                log.info("리스트 = {}", menuRecommends.getContent());
            }

            int nowPage = pageable.getPageNumber() + 1;

            int startPage = Math.max(1, nowPage - 2);
            int endPage = Math.min(nowPage + 2, menuRecommends.getTotalPages());

            model.addAttribute("nowPage", nowPage);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);


            model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
            model.addAttribute("next", pageable.next().getPageNumber());

            model.addAttribute("hasPrev", menuRecommends.hasPrevious());
            model.addAttribute("hasNext", menuRecommends.hasNext());

            log.info("nowPage = {}", nowPage);
            log.info("startPage = {}", startPage);
            log.info("endPage = {}", endPage);

            log.info("previous = {}", pageable.previousOrFirst().getPageNumber());
            log.info("next = {}", pageable.next().getPageNumber());


            log.info("hasPrev = {}", menuRecommends.hasPrevious());
            log.info("hasNext = {}", menuRecommends.hasNext());
        }


        return "/menurecommend/menu_recommend";
    }


    @GetMapping("/upload")
    public String menuRecommendUploadForm(@SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                          @ModelAttribute(value = "uploadForm") MenuRecommendUploadForm uploadForm,
                                          Model model) {
        // 세션에 회원 데이터가 없으면 홈 화면으로 이동
        if(loginMember == null) {
            log.info("로그인 상태가 아님");

            model.addAttribute("message", "회원만 글을 작성할 수 있습니다. 로그인 먼저 해주세요!");
            model.addAttribute("redirectUrl", "/members/login");
            return "messages";
        }
        return "/menurecommend/menu_recommend_upload";
    }

    @PostMapping("/upload")
    public String menuRecommendUpload(@SessionAttribute(LoginSessionConst.LOGIN_MEMBER) Member loginMember,
                                      @Valid @ModelAttribute(value = "uploadForm", binding = true) MenuRecommendUploadForm uploadForm, BindingResult bindingResult,
                                      Model model) throws IOException {
        log.info("바인딩 에러 정보 = {}", bindingResult);
        if (bindingResult.hasErrors()) {
            return "/menurecommend/menu_recommend_upload";
        }
        log.info("바인딩 에러 정보 = {}", uploadForm.getImageFile().getOriginalFilename());

        if (uploadForm.getImageFile().getOriginalFilename().equals("")) {
            model.addAttribute("message", "메뉴 사진을 넣어주세요!");
            model.addAttribute("redirectUrl", "/menu/upload");
            return "messages";
        }

        menuRecommendService.uploadMenu(loginMember.getId(), uploadForm);
        return "redirect:/menu";
    }

}
