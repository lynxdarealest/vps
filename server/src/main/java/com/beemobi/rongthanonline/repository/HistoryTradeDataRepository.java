package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.history.HistoryTradeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryTradeDataRepository extends JpaRepository<HistoryTradeData, Long> {
}
