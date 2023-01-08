package kyk.SpringFoodWorldProject.member.controller;

import kyk.SpringFoodWorldProject.member.domain.dto.LoginForm;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.SpringDataJpaMemberRepository;
import kyk.SpringFoodWorldProject.member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberServiceImpl memberService;

    private final SpringDataJpaMemberRepository memberRepository;



    /**
     *  회원 등록 폼
     */
    @GetMapping("/register")
    public String memberRegisterForm(@ModelAttribute("member") Member member) {
        return "members/member_register";
    }

    /**
     *  회원 저장 기능
     */
    @PostMapping("/register")
    public String save(@Valid @ModelAttribute Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/member_register";
        }

        memberRepository.save(member);
        return "redirect:/";
    }


    /**
     *  로그인 폼
     */
    @GetMapping("/login")
    public String memberLoginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "members/member_login";
    }

    /**
     *  로그인 처리 기능
     */
    // 서블릿에서도 세션을 지원하는 방식 (HttpServletRequest로 활용)
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form,
                        BindingResult bindingResult,
                        HttpServletRequest request) {
        // 검증 실패 : 오류가 있으면 폼으로 반환
        if (bindingResult.hasErrors()) {
            return "members/member_login";
        }

        // 성공 로직 : 로그인 기능 적용후 멤버에 저장
        Member loginMember = memberService.login(form.getLoginId(), form.getPassword()); // 폼에 입력한 아이디 패스워드 가져와서 멤버로 저장
        log.info("login? {}", loginMember);

        // null 값이면 로그인 실패 글로벌 오류(오브젝트 오류) 처리 : 글로벌 오류는 DB의 값에서 처리하는 이런 경우도 있어 자바코드로 구현하는게 바람직하다.
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "members/member_login";
        }

        // 로그인 성공 처리
        // HttpSession 객체에  request.getSession()로 담으면 모든 것이 해결 됨
        // 세션이 있으면 있는 세션을 반환하고 없으면 신규 세션을 생성한다. true일 때
        HttpSession session = request.getSession(); // 기본 값이 true이고, false는 없으면 생성 안함
        // 세션에 로그인 회원 정보를 보관한다.
        session.setAttribute("loginMember", loginMember);
        log.info("로그인 성공");
        log.info("sessionId={}", session.getId());

        return "redirect:/";
    }

    /**
     * 로그아웃 기능
     */
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) { // SessionManager에서 request로 사용
        // false는 세션이 있으면 기존 세션 반환, 세션이 없으면 null을 반환
        // true는 세션이 있으면 기존 세션 반환, 세션이 없으면 새로운 세션 생성
        HttpSession session = request.getSession(false); // 로그아웃은 없으면 생성할 필요 없기에 false를 넣음

        if (session != null) { // 세션이 있으면
            session.invalidate(); // 해당 세션이랑 그 안의 데이터를 모두 지운다.
            log.info("로그아웃 완료");
        }

        return "redirect:/";
    }




}