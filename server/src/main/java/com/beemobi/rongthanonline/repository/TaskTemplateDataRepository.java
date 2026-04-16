package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.TaskTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskTemplateDataRepository extends JpaRepository<TaskTemplateData, Integer> {
}
