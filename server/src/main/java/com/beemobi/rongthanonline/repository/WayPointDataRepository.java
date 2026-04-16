package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.WayPointData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WayPointDataRepository extends JpaRepository<WayPointData, Integer> {
}
