package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping
public class LoginController {

    private final LoginService loginService;

    @GetMapping("login")
    public String login(@ModelAttribute("loginForm") LoginForm form) {
        return "/login/loginForm";
    }

    @PostMapping("/login")
    public String loginRequest(@Valid @ModelAttribute LoginForm form, BindingResult result) {
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

        // TODO 로그인 성공시 처리

        return "redirect:/";
    }
}
