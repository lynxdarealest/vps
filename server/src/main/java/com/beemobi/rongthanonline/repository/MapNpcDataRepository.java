package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.MapNpcData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapNpcDataRepository extends JpaRepository<MapNpcData, Integer> {
}
