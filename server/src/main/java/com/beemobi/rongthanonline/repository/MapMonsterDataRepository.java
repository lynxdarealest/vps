package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.MapMonsterData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapMonsterDataRepository extends JpaRepository<MapMonsterData, Integer> {
}
