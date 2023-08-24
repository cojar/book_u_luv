package com.project.bookuluv.domain.admin.service;

import com.project.bookuluv.domain.admin.domain.Notice;
import com.project.bookuluv.domain.admin.dto.NoticeDto;
import com.project.bookuluv.domain.admin.repository.NoticeRepository;
import com.project.bookuluv.domain.member.domain.Member;
import com.project.bookuluv.domain.member.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<Notice> getAll() {
        return this.noticeRepository.findAll();
    }

    public Notice getById(Long id) {
        Optional<Notice> notice = this.noticeRepository.findById(id);
        if (notice.isPresent()) {
//            Notice notice1 = notice.get();
//            notice1.setHit(notice1.getHit()+1);
//            this.noticeRepository.save(notice1);
            return notice.get();
        } else {
            throw new DataNotFoundException("notice not found");
        }
    }
    public void incrementHitCount(Long id) {
        Notice notice = noticeRepository.getById(id);
        int currentHit = notice.getHit();
        notice.setHit(currentHit + 1);
        noticeRepository.save(notice);
    }

    public void create(String subject, String content, Member member) {
        Notice notice = Notice.builder()
                .subject(subject)
                .content(content)
                .register(member)
                .createDate(LocalDateTime.now())
                .build();
        this.noticeRepository.save(notice);
    }

    public void modify(NoticeDto noticeDto, Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        Notice modifiedNotice = notice.toBuilder()
                .subject(noticeDto.getSubject())
                .content(noticeDto.getContent())
                .modifyDate(LocalDateTime.now())
                .build();
        this.noticeRepository.save(modifiedNotice);
    }

    public void delete(Notice notice) {
        this.noticeRepository.delete(notice);
    }
}
