package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class ErrorPageController {

    public static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
    public static final String ERR0R_EXCEPTION = "javax.servlet.error.exception";
    public static final String ERROR_MESSAGE = "javax.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";

    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 404");
        printErrorInfo(request);
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 500");
        printErrorInfo(request);
        return "error-page/500";
    }

    @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> errorPage500Api(HttpServletRequest request, HttpServletResponse response) {
        log.info("Api error 500");

        Map<String, Object> result = new HashMap<>();

        Exception ex = (Exception) request.getAttribute(ERR0R_EXCEPTION);
        result.put("status", request.getAttribute(ERROR_STATUS_CODE));
        result.put("message", ex.getMessage());


        if (ex instanceof IllegalArgumentException) {
            response.setStatus(400);
        }

        return result;
    }


    private void printErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION_TYPE={}",request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERR0R_EXCEPTION={}",request.getAttribute(ERR0R_EXCEPTION));
        log.info("ERROR_MESSAGE={}",request.getAttribute(ERROR_MESSAGE));
        log.info("ERROR_REQUEST_URI={}",request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME={}",request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE={}",request.getAttribute(ERROR_STATUS_CODE));
        log.info("dispatchType={}",request.getDispatcherType());
    }
}
