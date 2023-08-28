package com.project.bookuluv.domain.admin.repository;

import com.project.bookuluv.domain.admin.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Notice findById(Integer id);

    @Query("select "
            + "distinct n "
            + "from Notice n "
            + "where "
            + "   (n.subject like %:kw% "
            + "   or n.content like %:kw% "
            + "   or n.noticeRegister like %:kw%)")
    Page<Notice> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

    @Query("select "
            + "distinct n "
            + "from Notice n "
            + "where "
            + "   (:field = 'title' and n.subject like %:kw%) "
            + "   or (:field = 'content' and n.content like %:kw%) "
            + "   or (:field = 'noticeRegister' and n.noticeRegister.nickName like %:kw%)")
    Page<Notice> findAllByKeywordAndField(@Param("kw") String kw, @Param("field") String field, Pageable pageable);
}
