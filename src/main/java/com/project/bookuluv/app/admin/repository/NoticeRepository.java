package com.project.bookuluv.app.admin.repository;

import com.project.bookuluv.app.article.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Notice findById(Integer id);
}
