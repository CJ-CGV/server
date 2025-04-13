package com.cj.cgv.domain.member;


import com.cj.cgv.domain.member.dto.MemberReq;
import com.cj.cgv.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cj.cgv.global.common.StatusCode.MEMBER_CREATE;

@Tag(name = "[회원]", description = "회원 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원 생성", description = "회원가입 합니다.")
    @PostMapping
    public ResponseEntity<CommonResponse<Member>> createMember(
            @RequestBody MemberReq memberReq){
        return ResponseEntity
                .status(MEMBER_CREATE.getStatus())
                .body(CommonResponse.from(MEMBER_CREATE.getMessage()
                        ,memberService.createMember(memberReq)));
    }
}
