package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.IntrinsicTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntrinsicRepository extends JpaRepository<IntrinsicTemplateData, Integer> {
}
