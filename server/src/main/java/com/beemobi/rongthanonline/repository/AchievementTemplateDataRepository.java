package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.AchievementTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementTemplateDataRepository extends JpaRepository<AchievementTemplateData, Integer> {
}
