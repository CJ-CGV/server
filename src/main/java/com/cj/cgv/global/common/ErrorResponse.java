package com.cj.cgv.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final String message;
    private final List<ValidationError> validationErrors;

    @Builder
    private ErrorResponse(String message, List<ValidationError> validationErrors) {
        this.message = message;
        this.validationErrors = validationErrors;
    }

    //ErrorResponse에서만 쓰이기 때문에 따로 클래스를 안만들고 내부 정적 클래스로 구현
    @Getter
    public static class ValidationError{

        private String field;
        private String value;
        private String reason;

        private ValidationError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
        /*
            1. BindingResult.getFieldErrors()를 사용해 BindingResult의 오류들을 가져온다.
            2. 필요한 값들을 Validation Error에 매핑하여 사용한다.

            Field : 예외가 발생한 field
            RejectedValue : 어떤 값으로 인해 예외가 발생하였는지
            DefaultMessage : 해당 예외가 발생했을 때 제공할 message 는 무엇인지
        */
        public static List<ValidationError> from(BindingResult bindingResult){
            List<FieldError> fieldErrors= bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new ValidationError(
                            error.getField(),
                            (error.getRejectedValue()==null) ? null : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
