package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.ItemTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemTemplateDataRepository extends JpaRepository<ItemTemplateData, Integer> {
}
