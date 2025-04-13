package com.cj.cgv.global.exception;


import com.cj.cgv.global.common.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    /*
        MethodArgumentNotValidException는 유효성 검사에서 실패하면 나타나는 예외로 bindingReult에 오류를 담는다.
        bindingResult가 없으면 400오류가 발생해 컨트롤러를 호출하지않고 오류페이지로 이동한다.
     */

//    // 유효성 검사 실패로 인한 예외로 메시지와 오류 객체도 담는다.
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<com.cj.cgv.global.common.ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e){
//        ErrorResponse errorResponse= ErrorResponse.builder()
//                .message(INPUT_VALUE_INVALID.getMessage())
//                .validationErrors(ErrorResponse.ValidationError.from(e.getBindingResult()))
//                .build();
//
//        return ResponseEntity.status(INPUT_VALUE_INVALID.getStatus())
//                .body(errorResponse);
//    }

//    // 데이터 무결성 문제로 인한 예외로, 유니크 제약조건이 있는 컬럼에 중복 값을 넣을 경우 생긴다.
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e){
//        ErrorResponse errorResponse= ErrorResponse.builder()
//                .message(DATA_INTEGRITY_VIOLATION.getMessage())
//                .build();
//
//        return ResponseEntity.status(DATA_INTEGRITY_VIOLATION.getStatus())
//                .body(errorResponse);
//    }

//    // requestPart로 받은 multiPartFile이 null이면 발생하는 예외
//    @ExceptionHandler(MissingServletRequestPartException.class)
//    public ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
//        ErrorResponse errorResponse= ErrorResponse.builder()
//                .message(MULTI_PART_FILE_INVALID.getMessage())
//                .build();
//        Sentry.captureException(e);
//
//        return ResponseEntity.status(MULTI_PART_FILE_INVALID.getStatus())
//                .body(errorResponse);
//    }


    // 무슨 예외가 터졌는지 메시지만 담는다.
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
        ErrorResponse errorResponse=ErrorResponse.builder()
                .message(e.getStatusCode().getMessage())
                .build();

        return ResponseEntity.status(e.getStatusCode().getStatus())
                .body(errorResponse);
    }
}
