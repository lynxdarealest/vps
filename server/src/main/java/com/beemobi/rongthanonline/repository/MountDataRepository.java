package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.MountData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MountDataRepository extends JpaRepository<MountData, Integer> {
}
