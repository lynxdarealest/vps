package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.MedalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedalDataRepository extends JpaRepository<MedalData, Integer> {
}
