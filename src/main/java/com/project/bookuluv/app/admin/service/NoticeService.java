package com.project.bookuluv.app.admin.service;

import com.project.bookuluv.app.admin.dto.NoticeDto;
import com.project.bookuluv.app.admin.repository.NoticeRepository;
import com.project.bookuluv.app.article.domain.Article;
import com.project.bookuluv.app.article.domain.Notice;
import com.project.bookuluv.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        NoticeDto noticeDto = NoticeDto.builder()
                .subject(subject)
                .content(content)
                .register(member)
                .build();
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
