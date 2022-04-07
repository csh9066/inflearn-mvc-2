package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private final Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    public void createSession(Object value, HttpServletResponse response) {
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);
        response.addCookie(new Cookie(SESSION_COOKIE_NAME, sessionId));
    }

    public Object getSession(HttpServletRequest request) {
        Cookie cookie = findCookie(request);
        if (cookie == null) return null;

        String sessionId = cookie.getValue();
        return sessionStore.get(sessionId);

    }

    private Cookie findCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(c -> c.getName().equals(SESSION_COOKIE_NAME))
                .findFirst()
                .orElse(null);
    }

    public void expire(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = findCookie(request);
        if (cookie == null) return;
        String sessionId = cookie.getValue();

        // 추출한 sessionId로 sessionStore에 매핑 되어있는 값을 삭제
        sessionStore.remove(sessionId);
        //
        Cookie expiredCookie = new Cookie(SESSION_COOKIE_NAME, null);
        expiredCookie.setMaxAge(0);
        response.addCookie(expiredCookie);
    }

}
