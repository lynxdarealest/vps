package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.OrderData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDataRepository extends JpaRepository<OrderData, Long> {

    List<OrderData> findByOrderIdAndType(Long orderId, Integer type);

    List<OrderData> findByUserIdAndStatusAndServer(Integer userId, Integer status, Integer server);
}
