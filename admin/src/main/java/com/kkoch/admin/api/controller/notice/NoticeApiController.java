package com.kkoch.admin.api.controller.notice;

import com.kkoch.admin.api.ApiResponse;
import com.kkoch.admin.api.controller.notice.response.NoticeResponse;
import com.kkoch.admin.api.service.notice.NoticeQueryService;
import com.kkoch.admin.domain.notice.repository.dto.NoticeSearchCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/admin-service/notices")
public class NoticeApiController {

    private final NoticeQueryService noticeQueryService;

    @GetMapping
    public ApiResponse<Page<NoticeResponse>> getNotices(
            @RequestParam Integer type,
            @RequestParam String keyword,
            @RequestParam Integer pageNum
    ) {
        log.info("<공지사항 목록 요청> Controller");
        NoticeSearchCond cond = getCond(type, keyword);

        PageRequest request = PageRequest.of(pageNum - 1, 10);

        Page<NoticeResponse> content = noticeQueryService.getNotices(cond, request);

        return ApiResponse.ok(content);
    }

    @GetMapping("/{noticeId}")
    public ApiResponse<NoticeResponse> getNotice(@PathVariable Long noticeId) {
        log.info("<공지사항 상세 조회> Controller");
        NoticeResponse content = noticeQueryService.getNotice(noticeId);
        return ApiResponse.ok(content);
    }

    private NoticeSearchCond getCond(int type, String keyword) {
        if (type == 1) {
            return NoticeSearchCond.builder()
                    .title(keyword)
                    .build();
        }
        return NoticeSearchCond.builder()
                .content(keyword)
                .build();
    }
}
