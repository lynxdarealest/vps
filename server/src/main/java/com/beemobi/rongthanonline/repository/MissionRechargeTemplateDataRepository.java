package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.MissionRechargeTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRechargeTemplateDataRepository extends JpaRepository<MissionRechargeTemplateData, Integer> {
}
