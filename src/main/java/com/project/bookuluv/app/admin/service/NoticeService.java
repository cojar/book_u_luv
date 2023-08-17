package com.project.bookuluv.app.admin.service;

import com.project.bookuluv.app.admin.repository.NoticeRepository;
import com.project.bookuluv.app.article.domain.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<Notice> getAll() {
        return this.noticeRepository.findAll();
    }
}
