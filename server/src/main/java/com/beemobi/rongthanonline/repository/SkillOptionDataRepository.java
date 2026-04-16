package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.SkillOptionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillOptionDataRepository extends JpaRepository<SkillOptionData, Integer> {
}
