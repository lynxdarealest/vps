package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.SkillTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillTemplateDataRepository extends JpaRepository<SkillTemplateData, Integer> {
}
