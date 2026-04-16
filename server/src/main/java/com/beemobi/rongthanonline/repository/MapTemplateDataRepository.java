package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.MapTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapTemplateDataRepository extends JpaRepository<MapTemplateData, Integer> {
}
