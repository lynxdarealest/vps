package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.MonsterTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonsterTemplateDataRepository extends JpaRepository<MonsterTemplateData, Integer> {
}
