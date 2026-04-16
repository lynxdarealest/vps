package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.GiftCodeHistoryData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiftCodeHistoryDataRepository extends JpaRepository<GiftCodeHistoryData, Long> {
    List<GiftCodeHistoryData> findByPlayerIdAndGiftCodeId(Integer playerId, Integer giftCodeId);
}
