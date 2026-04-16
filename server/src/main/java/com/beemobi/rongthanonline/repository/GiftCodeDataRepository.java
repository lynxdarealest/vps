package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.GiftCodeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftCodeDataRepository extends JpaRepository<GiftCodeData, Integer> {
    List<GiftCodeData> findByServer(Integer server);
}
