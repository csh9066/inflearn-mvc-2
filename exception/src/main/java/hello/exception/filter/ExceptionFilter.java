package hello.exception.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

@Slf4j
public class ExceptionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            log.info("doFilter");
            chain.doFilter(request,response);
        } catch (IOException e) {
            log.error("IOException",e);
        } catch (ServletException e) {
            log.error("ServletException",e);
        }

    }
}
