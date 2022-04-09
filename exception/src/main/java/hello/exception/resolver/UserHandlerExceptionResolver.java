package hello.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof UserException) {
                String acceptHeader = request.getHeader("accept");
                if (APPLICATION_JSON_VALUE.equals(acceptHeader)) {
                    log.info("{} equal {}", APPLICATION_JSON_VALUE, acceptHeader);

                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());

                    String result = objectMapper.writeValueAsString(errorResult);
                    log.info("result={}", result);
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("utf-8");
                    response.setStatus(400);
                    response.getWriter().write(result);
                    return new ModelAndView();
                } else {
                    return new ModelAndView("error/500");
                }

            }
        } catch (IOException e) {
            log.info("resolver ex", e);
        }
        return null;
    }
}
