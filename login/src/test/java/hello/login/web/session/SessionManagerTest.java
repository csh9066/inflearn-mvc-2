package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest() {
        // 세션 생성
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member();
        sessionManager.createSession(member, response);

        // 요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        Object sessionMember = (Member) sessionManager.getSession(request);
        Assertions.assertThat(sessionMember).isEqualTo(member);
        Assertions.assertThat(sessionMember).isNotNull();

        sessionManager.expire(request, response);
        Object expiredMember = (Member) sessionManager.getSession(request);
        Assertions.assertThat(expiredMember).isNull();
    }

}