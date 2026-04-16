package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.NpcTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NpcTemplateDataRepository extends JpaRepository<NpcTemplateData, Integer> {

}
