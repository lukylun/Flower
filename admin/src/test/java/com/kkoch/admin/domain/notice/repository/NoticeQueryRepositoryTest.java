package com.kkoch.admin.domain.notice.repository;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.controller.notice.response.NoticeResponse;
import com.kkoch.admin.domain.admin.Admin;
import com.kkoch.admin.domain.admin.repository.AdminRepository;
import com.kkoch.admin.domain.notice.Notice;
import com.kkoch.admin.domain.notice.repository.dto.NoticeSearchCond;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Transactional
class NoticeQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private NoticeQueryRepository noticeQueryRepository;
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private AdminRepository adminRepository;

    @DisplayName("[공지사항] 전체조회")
    @Test
    void getAllNotices() {
        Admin admin = insertAdmin();

        insertNotice(admin, true, "공지 사항", "공지 내용");
        insertNotice(admin, true, "공지 사항", "공지 내용");
        insertNotice(admin, true, "공지 사항", "공지 내용");
        insertNotice(admin, false, "공지 사항", "공지 내용");
        insertNotice(admin, true, "공지 사항", "공지 내용");
        insertNotice(admin, true, "공지 사항", "공지 내용");

        List<NoticeResponse> list = noticeQueryRepository.getAllNotices();

        assertThat(list).hasSize(6);
    }

    @DisplayName("[공지사항] 검색조건 조회")
    @Test
    void getNoticeByCondition() {
        Admin admin = insertAdmin();

        insertNotice(admin, true, "검색", "내용검색");
        insertNotice(admin, true, "공지 사항1", "공지 내용검색");
        insertNotice(admin, true, "공지 사항1", "내용검색");
        insertNotice(admin, true, "공지 사항1", "공지 내용");
        insertNotice(admin, true, "공지 사항1", "공지 내용");
        insertNotice(admin, true, "공지 사항1", "공지 내용");
        insertNotice(admin, false, "공지 검색s", "공지 내용");
        insertNotice(admin, true, "검색 사항1", "공지 내용");

        NoticeSearchCond cond = NoticeSearchCond.builder()
                .title("검색")
                .content("내용검색")
                .build();
        PageRequest pageRequest = PageRequest.of(0, 20);

        List<NoticeResponse> list = noticeQueryRepository.getNoticeByCondition(cond, pageRequest);

        assertThat(list).hasSize(4);
    }

    @DisplayName("[공지사항] 상세조회")
    @Test
    void getNotice() {
        Admin admin = insertAdmin();

        Notice notice = insertNotice(admin, true, "공지 사항", "공지 내용");

        Optional<NoticeResponse> findNotice = noticeQueryRepository.getNotice(notice.getId());

        assertThat(findNotice).isPresent();
    }

    private Notice insertNotice(Admin admin, boolean active, String title, String content) {
        return noticeRepository.save(Notice.builder()
                .title(title)
                .content(content)
                .admin(admin)
                .active(active)
                .build());
    }

    private Admin insertAdmin() {
        Admin admin = Admin.builder()
                .loginId("admin")
                .loginPw("admin123!")
                .name("관리자")
                .position("10")
                .tel("010-0000-0000")
                .active(true)
                .build();
        return adminRepository.save(admin);
    }
}