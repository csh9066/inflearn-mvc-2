package hello.itemservice.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource messageSource;

    @Test
    void notFoundMessage() {
        assertThatThrownBy(() -> messageSource.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    void notFoundMessageCodeDefaultMessage() {
        String message = messageSource.getMessage("no_code", null, "기본 메시지", null);
        assertThat(message).isEqualTo("기본 메시지");
    }

    @Test
    void helloMessageWhenLocalKorean() {
        String hello = messageSource.getMessage("hello", null, Locale.KOREAN);
        assertThat(hello).isEqualTo("안녕하세요");
    }

    @Test
    void helloNameMessageWhenLocalKorean() {
        String message = messageSource.getMessage("hello.name", new String[]{"메시지"}, Locale.KOREA);
        assertThat(message).isEqualTo("안녕하세요 메시지");
    }

    @Test
    void helloMessageWhenLocalEng() {
        String hello = messageSource.getMessage("hello", null, Locale.ENGLISH);
        assertThat(hello).isEqualTo("hello");
    }
}
