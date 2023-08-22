package com.project.bookuluv.domain.admin.repository;

import com.project.bookuluv.domain.admin.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Notice findById(Integer id);

}
