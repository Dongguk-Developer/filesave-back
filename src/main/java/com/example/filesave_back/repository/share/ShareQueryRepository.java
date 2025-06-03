package com.example.filesave_back.repository.share;

import com.example.filesave_back.entity.share.Share;
import com.example.filesave_back.projection.store.StoreProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShareQueryRepository extends JpaRepository<Share, Long> {
    Optional<List<Share>> findByCode(String code);
//    Optional<List<StoreProjection.FileSummary>> findAllByCode(String code);
//    List<StoreProjection.FileSummary> findByCode(String code);

}

