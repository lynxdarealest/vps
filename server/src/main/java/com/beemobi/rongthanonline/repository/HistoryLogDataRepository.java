package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.history.HistoryLogData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryLogDataRepository extends JpaRepository<HistoryLogData, Long> {
}
