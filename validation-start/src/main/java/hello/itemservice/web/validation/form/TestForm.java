package hello.itemservice.web.validation.form;


import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
public class TestForm {
    @Valid
    private Test test;

    @NotNull
    private List<@Max(2) Integer> tests;

    @Data
    static class Test {
        @NotBlank
        private String test;
    }
}
