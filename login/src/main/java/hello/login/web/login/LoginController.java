package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/login")
@Controller
public class LoginController {
    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "login/loginForm";
    }

//    @PostMapping
    public String loginV1(@Validated @ModelAttribute("loginForm") LoginForm loginForm,
                          BindingResult bindingResult,
                          HttpServletResponse response
    ) {
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (isNull(loginMember)) {
            bindingResult.reject("loginFail");
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}",bindingResult.getAllErrors());
            return "login/loginForm";
        }

        Cookie cookie = new Cookie("mySessionId", loginMember.getId().toString());
        response.addCookie(cookie);
        return "redirect:/";

    }

//    @PostMapping
    public String loginV2(@Validated @ModelAttribute("loginForm") LoginForm loginForm,
                        BindingResult bindingResult,
                        HttpServletResponse response
                        ) {
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (isNull(loginMember)) {
            bindingResult.reject("loginFail");
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}",bindingResult.getAllErrors());
            return "login/loginForm";
        }

        sessionManager.createSession(loginMember, response);
        return "redirect:/";

    }

//    @PostMapping
    public String loginV3(@Validated @ModelAttribute("loginForm") LoginForm loginForm,
                          BindingResult bindingResult,
                          HttpServletRequest request
    ) {
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (isNull(loginMember)) {
            bindingResult.reject("loginFail");
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult.getAllErrors());
            return "login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";

    }

    @PostMapping
    public String loginV4(@Validated @ModelAttribute("loginForm") LoginForm loginForm,
                          BindingResult bindingResult,
                          @RequestParam(defaultValue = "/", name = "redirectURL") String redirectURL,
                          HttpServletRequest request
    ) {
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if (isNull(loginMember)) {
            bindingResult.reject("loginFail");
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult.getAllErrors());
            return "login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:" + redirectURL;

    }
}
