package kyk.SpringFoodWorldProject.menu.controller;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.member.domain.LoginSessionConst;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.service.MemberServiceImpl;
import kyk.SpringFoodWorldProject.menu.domain.dto.MenuRecommendUpdateForm;
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
    private final MenuRecommendServiceImpl menuRecommendService;


    @GetMapping("")
    public String menuRecommend(@SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                @PageableDefault(page = 0, size = 9, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
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
        if(loginMember == null) {
            log.info("로그인 상태가 아님");

            model.addAttribute("message", "회원만 글을 작성할 수 있습니다. 로그인 먼저 해주세요!");
            model.addAttribute("redirectUrl", "/members/login");
            return "messages";
        }

        log.info("바인딩 에러 정보 = {}", bindingResult);
        if (bindingResult.hasErrors()) {
            return "/menurecommend/menu_recommend_upload";
        }

        // 파일을 필수로 넣게하기 위한 방지 작업
        // Multipart는 파일을 넣지 않아도 임의의 객체를 생성하므로 bindingResult로 검출할 수가 없다.
        if (uploadForm.getImageFile().getOriginalFilename().equals("")) {
            model.addAttribute("message", "메뉴 사진을 넣어주세요!");
            model.addAttribute("redirectUrl", "/menu/upload");
            return "messages";
        }

        menuRecommendService.uploadMenu(loginMember.getId(), uploadForm);
        return "redirect:/menu";
    }

    @GetMapping("/{menuRecommendId}/edit")
    public String updateMenuForm(@PathVariable Long menuRecommendId,
                                 @SessionAttribute(LoginSessionConst.LOGIN_MEMBER) Member loginMember,
                                 Model model) {
        if(loginMember == null) {
            log.info("로그인 상태가 아님");

            model.addAttribute("message", "회원만 글을 작성할 수 있습니다. 로그인 먼저 해주세요!");
            model.addAttribute("redirectUrl", "/members/login");
            return "messages";
        }

        MenuRecommend findMenuRecommend = menuRecommendService.findById(menuRecommendId).orElseThrow(() ->
                new IllegalArgumentException("메뉴 조회 실패: " + menuRecommendId));
        String storedFileName = findMenuRecommend.getStoredFileName();
        model.addAttribute("updateForm", findMenuRecommend);
        model.addAttribute("storedFileName", storedFileName);

        return "/menurecommend/menu_recommend_update";
    }

    @PostMapping("/{menuRecommendId}/edit")
    public String updateMenu(@PathVariable Long menuRecommendId,
                             @Valid @ModelAttribute(value = "updateForm") MenuRecommendUpdateForm updateForm, BindingResult bindingResult,
                             @SessionAttribute(LoginSessionConst.LOGIN_MEMBER) Member loginMember,
                             Model model) throws IOException {
        log.info("바인딩 에러 정보 = {}", bindingResult);
        if (bindingResult.hasErrors()) {
            MenuRecommend findMenuRecommend = menuRecommendService.findById(menuRecommendId).orElseThrow(() ->
                    new IllegalArgumentException("메뉴 조회 실패: " + menuRecommendId));
            String storedFileName = findMenuRecommend.getStoredFileName();
            model.addAttribute("storedFileName", storedFileName);

            return "/menurecommend/menu_recommend_update";
        }

        menuRecommendService.updateMenu(menuRecommendId, updateForm);

        return "redirect:/menu";
    }


    @GetMapping("/{menuRecommendId}/delete")
    public String delete(@PathVariable Long menuRecommendId,
                         @SessionAttribute(LoginSessionConst.LOGIN_MEMBER) Member loginMember,
                         Model model) throws IOException {
        menuRecommendService.delete(menuRecommendId);

        return "redirect:/menu";
    }
}
