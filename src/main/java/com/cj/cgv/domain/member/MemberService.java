package com.cj.cgv.domain.member;


import com.cj.cgv.domain.member.dto.MemberReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Member createMember(MemberReq memberReq){
        return memberRepository.save(memberReq.toEntity());
    }
}
