package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.MissionItemWeeklyTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionItemWeeklyTemplateDataRepository extends JpaRepository<MissionItemWeeklyTemplateData, Integer> {
}
