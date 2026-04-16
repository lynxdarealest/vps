package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.LevelData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelDataRepository extends JpaRepository<LevelData, Integer> {
}
