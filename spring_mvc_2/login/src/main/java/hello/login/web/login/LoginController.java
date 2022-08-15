package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping
public class LoginController {

    private final LoginService loginService;

    @GetMapping("login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "/login/loginForm";
    }

    //    @PostMapping("/login")
    public String loginUseCookie(@Valid @ModelAttribute LoginForm form, BindingResult result, HttpServletResponse response) {
        if (result.hasErrors()) {
            return "/login/loginForm";
        }

        log.info("login try {}", form.getLoginId());
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if (loginMember == null) {
            result.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }
        log.info("login success{}", loginMember);

        Cookie idCookie = new Cookie("memberId",
                String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);

        return "redirect:/";
    }

    //    @PostMapping("/logout")
    public String logoutUseCookie(HttpServletResponse response) {
        expiredCookie(response, "memberId");
        return "redirect:/";
    }

    private void expiredCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @PostMapping("/login")
    public String loginUseSession(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                                  HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutUseSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }


}
