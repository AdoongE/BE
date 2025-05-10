package com.adoonge.seedzip.notice.repository;

import com.adoonge.seedzip.notice.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> findByIsVisibleTrue(Pageable pageable);

}
