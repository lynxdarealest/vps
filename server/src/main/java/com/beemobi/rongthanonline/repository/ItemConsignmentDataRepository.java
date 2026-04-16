package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.ItemConsignmentData;
import com.beemobi.rongthanonline.item.ItemConsignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Transactional
@Repository
public interface ItemConsignmentDataRepository extends JpaRepository<ItemConsignmentData, Long> {
    List<ItemConsignmentData> findByServerAndStatusIn(Integer server, ItemConsignmentStatus[] statusList);

    @Modifying
    @Query("UPDATE ItemConsignmentData i SET i.buyerId = :buyerId, " +
            "i.status = :status, " +
            "i.item = :item, " +
            "i.buyerName = :buyerName, " +
            "i.buyTime = :buyTime, " +
            "i.receiveTime = :receiveTime " +
            "WHERE i.id = :id")
    void saveData(Long id, Integer buyerId, ItemConsignmentStatus status, String item, String buyerName, Timestamp buyTime, Timestamp receiveTime);
}
