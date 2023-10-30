package com.kkoch.admin.api.service.notice;

import com.kkoch.admin.api.service.notice.dto.AddNoticeDto;
import com.kkoch.admin.api.service.notice.dto.SetNoticeDto;
import com.kkoch.admin.domain.notice.Notice;
import com.kkoch.admin.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Long addNotice(Long adminId, AddNoticeDto dto) {
        Notice notice = dto.toEntity(adminId);
        Notice savedNotice = noticeRepository.save(notice);
        return savedNotice.getId();
    }

    public Long setNotice(Long noticeId, SetNoticeDto dto) {
        Notice findNotice = getNoticeEntity(noticeId);

        findNotice.edit(dto.getTitle(), dto.getContent());

        return findNotice.getId();
    }

    public Long removeNotice(Long noticeId) {
        Notice findNotice = getNoticeEntity(noticeId);

        findNotice.remove();

        return findNotice.getId();
    }

    private Notice getNoticeEntity(Long noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(NoSuchElementException::new);
    }
}
