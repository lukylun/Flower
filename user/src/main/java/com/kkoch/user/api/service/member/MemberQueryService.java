package com.kkoch.user.api.service.member;

import com.kkoch.user.api.controller.member.response.MemberInfoResponse;
import com.kkoch.user.api.controller.member.response.MemberResponseForAdmin;
import com.kkoch.user.domain.member.repository.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 회원 Query 서비스
 *
 * @author 임우택
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberQueryRepository memberQueryRepository;

    /**
     * 회원 정보 조회
     *
     * @param memberKey 회원 고유키
     * @return 회원 정보
     */
    public MemberInfoResponse getMyInfo(String memberKey) {
        return memberQueryRepository.findMyInfoByMemberKey(memberKey);
    }

    /**
     * 이메일 중복 검증
     *
     * @param email 이메일
     * @return 이메일 중복 여부
     */
    public boolean validationEmail(String email) {
        return memberQueryRepository.existEmail(email);
    }

    public List<MemberResponseForAdmin> getUsers() {
        return memberQueryRepository.findAllUser();
    }
}
