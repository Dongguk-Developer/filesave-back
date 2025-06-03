package com.example.filesave_back.repository.store;

import com.example.filesave_back.entity.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreCommandRepository extends JpaRepository<Store, Long> {
}
