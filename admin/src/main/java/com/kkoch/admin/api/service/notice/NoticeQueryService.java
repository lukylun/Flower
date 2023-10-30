package com.kkoch.admin.api.service.notice;

import com.kkoch.admin.api.controller.notice.response.NoticeResponse;
import com.kkoch.admin.domain.notice.repository.NoticeQueryRepository;
import com.kkoch.admin.domain.notice.repository.dto.NoticeSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NoticeQueryService {

    private final NoticeQueryRepository noticeQueryRepository;

    public List<NoticeResponse> getAllNotices() {
        return noticeQueryRepository.getAllNotices();
    }

    public Page<NoticeResponse> getNotices(NoticeSearchCond cond, Pageable pageable) {
        List<NoticeResponse> content = noticeQueryRepository.getNoticeByCondition(cond, pageable);

        int startNumber = pageable.getPageNumber() * pageable.getPageSize() + 1;
        for (NoticeResponse response : content) {
            response.setNo(startNumber++);
        }

        long totalCount = noticeQueryRepository.getTotalCount(cond);

        return new PageImpl<>(content, pageable, totalCount);
    }

    public NoticeResponse getNotice(Long noticeId) {
        return noticeQueryRepository.getNotice(noticeId)
                .orElseThrow(NoSuchElementException::new);
    }
}
