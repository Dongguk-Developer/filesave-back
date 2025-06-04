package com.example.filesave_back.repository.store;

import com.example.filesave_back.entity.store.Store;
import com.example.filesave_back.projection.store.StoreProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StoreQueryRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByFilenameAndCreatedAt(String filename, LocalDateTime createdAt);
}
