package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.MissionWeekTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionWeekTemplateDataRepository extends JpaRepository<MissionWeekTemplateData, Integer> {
}
