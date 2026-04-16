package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.SkillOptionTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillOptionTemplateDataRepository extends JpaRepository<SkillOptionTemplateData,Integer> {
}
