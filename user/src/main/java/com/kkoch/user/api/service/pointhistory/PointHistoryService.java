package com.kkoch.user.api.service.pointhistory;

import com.kkoch.user.api.controller.pointhistory.response.AddPointHistoryResponse;
import com.kkoch.user.api.service.pointhistory.dto.AddPointHistoryDto;
import com.kkoch.user.domain.member.Member;
import com.kkoch.user.domain.member.repository.MemberRepository;
import com.kkoch.user.domain.pointhistory.PointHistory;
import com.kkoch.user.domain.pointhistory.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

/**
 * 포인트 내역 Command 서비스
 *
 * @author 임우택
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;
    private final MemberRepository memberRepository;

    /**
     * 포인트 내역 등록
     *
     * @param memberKey 회원 고유키
     * @param dto 저장할 데이터
     * @return 저장된 포인트 내역 결과
     */
    public AddPointHistoryResponse addPointHistory(String memberKey, AddPointHistoryDto dto) {
        Member member = memberRepository.findByMemberKey(memberKey)
            .orElseThrow(NoSuchElementException::new);

        PointHistory pointHistory = dto.toEntity(member);
        PointHistory savedPointHistory = pointHistoryRepository.save(pointHistory);
        return AddPointHistoryResponse.of(savedPointHistory);
    }
}
