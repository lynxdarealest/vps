package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.history.HistoryBuyItemData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryBuyItemDataRepository  extends JpaRepository<HistoryBuyItemData, Long> {
}
