package com.project.bookuluv.domain.admin.repository;

import com.project.bookuluv.domain.article.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Notice findById(Integer id);
}
