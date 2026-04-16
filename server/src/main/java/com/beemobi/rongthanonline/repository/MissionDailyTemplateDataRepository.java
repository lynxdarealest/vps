package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.MissionDailyTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionDailyTemplateDataRepository extends JpaRepository<MissionDailyTemplateData, Integer> {
}
