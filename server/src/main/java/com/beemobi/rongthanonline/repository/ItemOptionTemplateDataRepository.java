package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.ItemOptionTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemOptionTemplateDataRepository extends JpaRepository<ItemOptionTemplateData, Integer> {
}
