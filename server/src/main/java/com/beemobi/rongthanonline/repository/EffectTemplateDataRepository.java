package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.EffectTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EffectTemplateDataRepository extends JpaRepository<EffectTemplateData, Integer> {
}
