package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String homeV1(Model model, @CookieValue(name = "mySessionId", required = false) Long memberId) {
        if (Objects.nonNull(memberId)) {
            Member member = memberRepository.findById(memberId);
            model.addAttribute("member", member);
            return "loginHome";
        }

        return "home";
    }

//    @GetMapping("/")
    public String homeV2(Model model, HttpServletRequest request) {
        Member member = (Member) sessionManager.getSession(request);
        log.info("sessionMember={}", member);
        if (Objects.isNull(member)) return "home";

        model.addAttribute("member", member);
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeV3(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (Objects.isNull(session)) return "home";

        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        model.addAttribute("member", member);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeV3Spring(Model model,
                               @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member) {
        if (Objects.isNull(member)) return "home";

        model.addAttribute("member", member);
        return "loginHome";
    }

//    @PostMapping("/logout")
    public String logoutV1(HttpServletResponse response) {
        Cookie cookie = new Cookie("mySessionId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "home";
    }

//    @PostMapping("/logout")
    public String logout2(HttpServletRequest request,HttpServletResponse response) {
        sessionManager.expire(request, response);
        return "home";
    }

    @PostMapping("/logout")
    public String logout3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }



}