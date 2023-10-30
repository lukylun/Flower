package com.kkoch.admin.api.service.notice;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.service.notice.dto.AddNoticeDto;
import com.kkoch.admin.api.service.notice.dto.SetNoticeDto;
import com.kkoch.admin.domain.admin.Admin;
import com.kkoch.admin.domain.admin.repository.AdminRepository;
import com.kkoch.admin.domain.notice.Notice;
import com.kkoch.admin.domain.notice.repository.NoticeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Transactional
class NoticeServiceTest extends IntegrationTestSupport {

    @Autowired
    private NoticeService noticeService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private NoticeRepository noticeRepository;

    @DisplayName("[공지사항 작성]")
    @Test
    void addNotice() {
        Admin admin = insertAdmin();
        AddNoticeDto dto = AddNoticeDto.builder()
                .title("공지 제목")
                .content("공지 내용")
                .build();

        Long noticeId = noticeService.addNotice(admin.getId(), dto);

        assertThat(noticeRepository.findById(noticeId)).isPresent();
    }

    @DisplayName("[공지사항 수정]")
    @Test
    void setNotice() {
        Admin admin = insertAdmin();
        Notice notice = insertNotice(admin, true, "공지 사항", "공지 내용");
        SetNoticeDto dto = SetNoticeDto.builder()
                .title("수정제목")
                .content("내용수정")
                .build();

        Long noticeId = noticeService.setNotice(notice.getId(), dto);

        Optional<Notice> findNotice = noticeRepository.findById(noticeId);

        assertThat(findNotice).isPresent();
        assertThat(findNotice.get().getTitle()).isEqualTo("수정제목");
    }

    @DisplayName("[공지사항 삭제]")
    @Test
    void removeNotice() {
        Admin admin = insertAdmin();
        Notice notice = insertNotice(admin, true, "공지 사항", "공지 내용");

        Long noticeId = noticeService.removeNotice(notice.getId());

        Optional<Notice> findNotice = noticeRepository.findById(noticeId);
        Notice setNotice = findNotice.get();

        assertThat(setNotice.isActive()).isFalse();

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