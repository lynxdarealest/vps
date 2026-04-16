package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.GiftData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftDataRepository extends JpaRepository<GiftData, Integer> {
}
