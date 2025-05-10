package com.adoonge.seedzip.notice.repository;

import com.adoonge.seedzip.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
