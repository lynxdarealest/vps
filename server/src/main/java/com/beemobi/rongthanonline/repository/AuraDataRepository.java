package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.AuraData;
import com.beemobi.rongthanonline.data.MedalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuraDataRepository extends JpaRepository<AuraData, Integer> {
}
