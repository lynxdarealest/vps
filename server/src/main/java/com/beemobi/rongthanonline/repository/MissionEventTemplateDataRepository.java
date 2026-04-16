package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.MissionDailyTemplateData;
import com.beemobi.rongthanonline.data.MissionEventTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionEventTemplateDataRepository extends JpaRepository<MissionEventTemplateData, Integer> {
}
