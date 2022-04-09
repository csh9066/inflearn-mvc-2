package hello.exception.api;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ErrorResult illegalArgumentException(IllegalArgumentException e) {
//        log.error("[exceptionHandler] ex", e);
//        return new ErrorResult("BAD", e.getMessage());
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResult> userException(UserException e) {
//        log.error("[userException] ex", e);
//        return new ResponseEntity<>(new ErrorResult("userException", e.getMessage()), HttpStatus.NOT_FOUND);
//    }
//
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler
//    public ErrorResult exHandler(Exception e) {
//        log.error("[exceptionHandler] ex", e);
//        return new ErrorResult("EX", "내보 오류");
//    }

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable String id) {
        log.info("/api2/members/");
        if (id.equals("ex")) {
            throw  new RuntimeException("잘못된 사용자");
        }

        if (id.equals("bad")) {
            throw  new IllegalArgumentException("잘못된 요청");
        }

        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        return new MemberDto(id, "hello" + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
