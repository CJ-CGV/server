package com.cj.cgv.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    // Movie
    MOVIE_CREATE(CREATED, "영화 생성 완료"),
    MOVIE_FOUND(OK, "영화 조회 완료"),
    MOVIE_UPDATE(OK, "영화 수정 완료"),
    MOVIE_DELETE(OK, "영화 삭제 완료"),
    MOVIE_ALREADY_EXISTS(BAD_REQUEST, "이미 존재하는 영화입니다."),


    // Schedule
    SCHEDULE_CREATE(CREATED, "스케줄 생성 완료"),
    SCHEDULE_FOUND(OK, "스케줄 조회 완료"),
    SCHEDULE_DELETE(OK, "스케줄 삭제 완료"),

    // Seat
    SEAT_CREATE(CREATED, "좌석 생성 완료"),
    SEAT_FOUND(OK, "좌석 조회 완료"),
    SEAT_DELETE(OK, "좌석 삭제 완료"),
    SEAT_SOLD_OUT(CONFLICT,"이미 선택된 좌석입니다."),

    // Member
    MEMBER_LOGIN_SUCCESS(OK, "로그인에 성공하였습니다."),
    MEMBER_FOUND(OK, "회원 조회 완료"),
    MEMBER_CREATE(OK,"회원 가입 완료"),

    // Reservation
    RESERVATION_FOUND(OK, "예매 내역 조회 완료"),
    RESERVATION_CREATE(OK, "예매 완료"),
    RESERVATION_DELETE(OK,"예매 취소 완료"),



    /* 400 BAD_REQUEST : 잘못된 요청 */
    INPUT_VALUE_INVALID(BAD_REQUEST,"유효하지 않은 입력입니다."),
    INVALID_STUDENT_ID(BAD_REQUEST,"유효하지 않은 학번입니다."),
    ADDRESS_INVALID(BAD_REQUEST,"유효하지 않은 주소입니다."),
    MULTI_PART_FILE_INVALID(BAD_REQUEST,"MultiPartFile이 존재하지 않습니다"),
    MULTI_PART_FILE_SEQUENCE_INVALID(BAD_REQUEST,"MultipartFile과 순서 리스트의 개수가 맞지 않습니다."),
    FILE_SAVE_INVALID(BAD_REQUEST,"파일 저장 중 오류가 발생했습니다."),
    FILE_DELETE_INVALID(BAD_REQUEST,"파일 삭제 중 오류가 발생했습니다."),
    DATA_INTEGRITY_VIOLATION(BAD_REQUEST,"이미 존재하는 값이거나 필수 필드에 null이 있습니다."),
    MARKET_NOT_DELETED(BAD_REQUEST,"사용자 요청매장과 정보가 일치하지 않아 삭제할 수 없습니다."),
    MARKET_SEARCH_NAME_INVALID(BAD_REQUEST,"검색어는 2글자 이상 입력해야 합니다."),

    /* 401 UNAUTHORIZED : 비인증 사용자 */
    UNAUTHORIZED_LOGIN_ERROR(UNAUTHORIZED, "학번 또는 비밀번호가 올바르지 않습니다."),
    INVALID_ACCESS_TOKEN(UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    /* 403 FORBIDDEN : 권한 없음 */

    /* 404 NOT_FOUND : 존재하지 않는 리소스 */
    MEMBER_NOT_EXIST(NOT_FOUND, "존재하지 않는 회원입니다."),
    MOVIE_NOT_EXIST(NOT_FOUND,"존재하지 않는 영화입니다."),
    SCHEDULE_NOT_EXIST(NOT_FOUND,"존재하지 않는 영화 일정입니다."),
    SEAT_NOT_EXIST(NOT_FOUND, "존재하지 않는 좌석입니다."),
    RESERVATION_IS_DELETED(NOT_FOUND, "이미 취소된 예매입니다."),
    RESERVATION_NOT_EXIST(NOT_FOUND,"존재하지 않는 예매입니다."),
    ADDRESS_NOT_EXIST(NOT_FOUND,"존재하지 않는 주소입니다."),

    /* 409 CONFLICT : 리소스 충돌 */
    COUPON_ALREADY_ISSUED(CONFLICT, "이미 발급된 쿠폰입니다."),
    COUPON_SOLD_OUT(CONFLICT, "쿠폰이 모두 소진되었습니다."),
    TICKET_SOLD_OUT(CONFLICT, "공감권이 소진되었습니다."),

    /* 503 UNAVAILABLE : 서비스 이용 불가  */
    FCM_UNAVAILABLE(SERVICE_UNAVAILABLE, "알림 기능을 이용할 수 없습니다.");


    private final HttpStatus status;
    private final String message;
}
