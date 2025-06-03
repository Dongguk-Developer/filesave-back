package com.example.filesave_back.repository.share;

import com.example.filesave_back.entity.share.Share;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareCommandRepository extends JpaRepository<Share, Long> {
}
