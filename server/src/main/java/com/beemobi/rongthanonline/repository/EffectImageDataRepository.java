package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.EffectImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EffectImageDataRepository extends JpaRepository<EffectImageData, Integer> {
}
