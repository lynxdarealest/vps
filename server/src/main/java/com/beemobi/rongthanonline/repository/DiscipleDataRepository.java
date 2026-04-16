package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.bot.disciple.DiscipleStatus;
import com.beemobi.rongthanonline.data.DiscipleData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Transactional
@Repository
public interface DiscipleDataRepository extends JpaRepository<DiscipleData, Integer> {
    @Modifying
    @Query("UPDATE DiscipleData d SET d.name = :name WHERE d.id = :id")
    void setName(Integer id, String name);

    @Modifying
    @Query("UPDATE DiscipleData d SET d.baseInfo = :baseInfo, d.powerInfo = :powerInfo, d.itemsBody = :itemsBody, d.itemsOther = :itemsOther, d.skillsInfo = :skillsInfo, d.mapInfo = :mapInfo, d.status = :status, d.type = :type WHERE d.id = :id")
    void saveData(Integer id, String baseInfo, String powerInfo, String itemsBody, String itemsOther, String skillsInfo, String mapInfo, DiscipleStatus status, Integer type);

    @Query(nativeQuery = true, value = "SELECT d.id, d.name AS disciple_name, d.gender, " +
            "CAST(JSON_UNQUOTE(JSON_EXTRACT(d.power_info, '$.power')) AS UNSIGNED) AS power, " +
            "CAST(JSON_UNQUOTE(JSON_EXTRACT(d.power_info, '$.level')) AS UNSIGNED) AS level, " +
            "p.name AS player_name, " +
            "CAST(JSON_UNQUOTE(JSON_EXTRACT(d.power_info, '$.power_time')) AS UNSIGNED) AS time " +
            "FROM rto_disciple d " +
            "INNER JOIN rto_player p " +
            "ON d.id = (-1) * p.id " +
            "WHERE p.server = ? " +
            "ORDER BY power DESC, time ASC " +
            "LIMIT ?")
    List<Object[]> findTopPower(Integer server, Integer limit);

}
