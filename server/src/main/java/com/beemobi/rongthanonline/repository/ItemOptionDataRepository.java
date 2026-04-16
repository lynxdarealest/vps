package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.ItemOptionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemOptionDataRepository extends JpaRepository<ItemOptionData, Integer> {
}
