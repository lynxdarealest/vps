package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.PlayerNameData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerNameDataRepository extends JpaRepository<PlayerNameData, Long> {
    List<PlayerNameData> findByName(String name);

    List<PlayerNameData> findByNameOrNameBase(String name, String nameBase);
}
