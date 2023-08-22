package com.project.bookuluv.domain.admin.service;

import com.project.bookuluv.domain.admin.domain.Notice;
import com.project.bookuluv.domain.admin.repository.NoticeRepository;
import com.project.bookuluv.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<Notice> getAll() {
        return this.noticeRepository.findAll();
    }

    public Notice getById(Integer id) {
        return this.noticeRepository.findById(id);
    }

    public void create(String subject, String content, Member member) {
        Notice notice = Notice.builder()
                .subject(subject)
                .content(content)
                .register(member)
                .build();
        this.noticeRepository.save(notice);
    }

    public void modify(String subject, String content, Notice notice) {
        notice.setSubject(subject);
        notice.setContent(content);
        notice.setModifyDate(LocalDateTime.now());
        this.noticeRepository.save(notice);
    }

    public void delete(Notice notice) {
        this.noticeRepository.delete(notice);
    }
}
