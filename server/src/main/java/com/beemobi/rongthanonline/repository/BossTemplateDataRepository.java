package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.BossTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BossTemplateDataRepository extends JpaRepository<BossTemplateData, Integer> {
}
