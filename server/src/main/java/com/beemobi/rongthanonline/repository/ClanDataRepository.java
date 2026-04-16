package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.ClanData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Transactional
@Repository
public interface ClanDataRepository extends JpaRepository<ClanData, Integer> {
    @Query(nativeQuery = true, value = "SELECT clan.leader_id , clan.name AS clan_name, clan.level, clan.exp, clan.update_time, " +
            "m.name AS leader_name, " +
            "m.gender " +
            "FROM rto_clan clan " +
            "INNER JOIN rto_clan_member m " +
            "ON clan.leader_id = m.player_id " +
            "WHERE clan.server = ? " +
            "ORDER BY clan.level DESC, clan.exp DESC, clan.update_time ASC " +
            "LIMIT ?")
    List<Object[]> findTopLevel(Integer server, Integer limit);

    @Modifying
    @Query("UPDATE ClanData c SET c.bonusSlot = :bonusSlot WHERE c.id = :id")
    void setBonusSlot(Integer id, Integer bonusSlot);

    @Modifying
    @Query("UPDATE ClanData c SET c.name = :name, " +
            "c.slogan = :slogan, " +
            "c.notification = :notification, " +
            "c.level = :level, " +
            "c.leaderId = :leaderId, " +
            "c.exp = :exp, " +
            "c.coin = :coin, " +
            "c.resetTime = :resetTime, " +
            "c.updateTime = :updateTime, " +
            "c.countManor = :countManor  " +
            "WHERE c.id = :id")
    void saveData(Integer id, String name, String slogan, String notification,
                  Integer level, Integer leaderId, Long exp, Long coin,
                  Timestamp resetTime, Timestamp updateTime, Integer countManor);
}
