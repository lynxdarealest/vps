package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.history.HistoryGiveDiamondData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryGiveDiamondDataRepository extends JpaRepository<HistoryGiveDiamondData, Long> {
}
