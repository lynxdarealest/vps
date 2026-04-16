package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.TaskStepTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskSubTemplateDataRepository extends JpaRepository<TaskStepTemplateData, Integer> {
}
