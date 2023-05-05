package kyk.SpringFoodWorldProject.exhandler.advice;

import kyk.SpringFoodWorldProject.exception.DuplicatedMemberLoginIdException;
import kyk.SpringFoodWorldProject.exception.DuplicatedMemberNameException;
import kyk.SpringFoodWorldProject.exception.MemberNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ExControllerAdvice {

    /**
     * 회원가입시 중복된 닉네임 또는 로그인 ID일 시 예외처리
     */
    @ExceptionHandler(value = { DuplicatedMemberLoginIdException.class, DuplicatedMemberNameException.class })
    public ModelAndView handleDuplicatedMemberException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("redirectUrl", "/members/join");
        modelAndView.setViewName("messages");

        return modelAndView;
    }


    /**
     * 로그인시 아이디 또는 패스워드 불일치 예외처리
     */
    @ExceptionHandler(MemberNotFoundException.class)
    public ModelAndView MemberNotFoundException(MemberNotFoundException ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        String requestURL = String.valueOf(request.getRequestURL());
        log.info("requestURL={}", requestURL);

        // 로그인 폼에서 발생한 경우
        if (requestURL.contains("/members/login")) {
            modelAndView.addObject("message", ex.getMessage());
            modelAndView.addObject("redirectUrl", "/members/login");
            modelAndView.setViewName("messages");
        }
        else { // 메인 폼에서 발생한 경우
            modelAndView.addObject("message", ex.getMessage());
            modelAndView.addObject("redirectUrl", "/");
            modelAndView.setViewName("messages");
        }

        return modelAndView;
    }
}
