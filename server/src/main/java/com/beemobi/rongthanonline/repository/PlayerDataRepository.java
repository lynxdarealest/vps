package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.PlayerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Transactional
@Repository
public interface PlayerDataRepository extends JpaRepository<PlayerData, Integer> {
    List<PlayerData> findByUserIdAndServer(Integer userId, Integer server);

    List<PlayerData> findByUserIdOrName(Integer userId, String name);

    List<PlayerData> findByName(String name);

    List<PlayerData> findByNameOrNameBase(String name, String nameBase);

    List<PlayerData> findByNameBase(String name);

    @Query(nativeQuery = true, value = "SELECT id, name, gender, pro " +
            "FROM rto_player " +
            "WHERE server = ? " +
            "ORDER BY pro DESC " +
            "LIMIT ?")
    List<Object[]> findTopPro(Integer server, Integer limit);

    @Query(nativeQuery = true, value = "SELECT id, name, gender, " +
            "CAST(JSON_UNQUOTE(JSON_EXTRACT(power_info, '$.power')) AS UNSIGNED) AS power, " +
            "CAST(JSON_UNQUOTE(JSON_EXTRACT(power_info, '$.level')) AS UNSIGNED) AS level, " +
            "CAST(JSON_UNQUOTE(JSON_EXTRACT(power_info, '$.power_time')) AS UNSIGNED) AS time " +
            "FROM rto_player " +
            "WHERE server = ? " +
            "ORDER BY power DESC, time ASC " +
            "LIMIT ?")
    List<Object[]> findTopPower(Integer server, Integer limit);

    @Query(nativeQuery = true, value = "SELECT id, name, gender, " +
            "CAST(JSON_UNQUOTE(JSON_EXTRACT(event, '$.point')) AS UNSIGNED) AS e_point, " +
            "CAST(JSON_UNQUOTE(JSON_EXTRACT(event, '$.update_time')) AS UNSIGNED) AS e_time " +
            "FROM rto_player " +
            "WHERE CAST(JSON_UNQUOTE(JSON_EXTRACT(event, '$.point')) AS UNSIGNED) > 0 AND server = ? " +
            "ORDER BY e_point DESC, e_time ASC " +
            "LIMIT ?")
    List<Object[]> findTopEvent(Integer server, Integer limit);

    @Query(nativeQuery = true, value = "SELECT id, name, gender, " +
            "CAST(JSON_UNQUOTE(JSON_EXTRACT(event, '$.point_other')) AS UNSIGNED) AS e_point, " +
            "CAST(JSON_UNQUOTE(JSON_EXTRACT(event, '$.other_time')) AS UNSIGNED) AS e_time " +
            "FROM rto_player " +
            "WHERE CAST(JSON_UNQUOTE(JSON_EXTRACT(event, '$.point_other')) AS UNSIGNED) > 0 AND server = ? " +
            "ORDER BY e_point DESC, e_time ASC " +
            "LIMIT ?")
    List<Object[]> findTopEventOther(Integer server, Integer limit);

    @Modifying
    @Query("UPDATE PlayerData p SET p.isLock = :isLock WHERE p.id = :id")
    void setLockNow(Integer id, Boolean isLock);

    @Modifying
    @Query("UPDATE PlayerData p SET p.isOnline = :isOnline, p.loginTime = :loginTime WHERE p.id = :id")
    void setOnline(Integer id, Boolean isOnline, Timestamp loginTime);

    @Modifying
    @Query("UPDATE PlayerData p SET p.clanInfo = :clanInfo WHERE p.id = :id")
    void setClanInfo(Integer id, String clanInfo);

    @Modifying
    @Query("UPDATE PlayerData p SET p.pin = :pin WHERE p.id = :id")
    void setPin(Integer id, String pin);

    @Modifying
    @Query("UPDATE PlayerData p SET p.lockInfo = :lockInfo WHERE p.id = :id")
    void setLockInfo(Integer id, String lockInfo);

    @Modifying
    @Query("UPDATE PlayerData p SET p.name = :name, p.nameBase = :nameBase WHERE p.id = :id")
    void setName(Integer id, String name, String nameBase);

    @Modifying
    @Query("UPDATE PlayerData p " +
            "SET " +
            "p.baseInfo = :baseInfo, " +
            "p.powerInfo = :powerInfo, " +
            "p.diamond = :diamond, " +
            "p.xu = :xu, " +
            "p.ruby = :ruby, " +
            "p.xuKhoa = :xuKhoa, " +
            "p.clanInfo = :clanInfo, " +
            "p.itemsBag = :itemsBag, " +
            "p.itemsBox = :itemsBox, " +
            "p.itemsBody = :itemsBody, " +
            "p.itemsPet = :itemsPet, " +
            "p.itemsOther = :itemsOther, " +
            "p.taskInfo = :taskInfo, " +
            "p.skillsInfo = :skillsInfo, " +
            "p.keysSkill = :keysSkill, " +
            "p.mapInfo = :mapInfo, " +
            "p.barrackInfo = :barrackInfo, " +
            "p.resetTime = :resetTime, " +
            "p.logoutTime = :logoutTime, " +
            "p.isOnline = :isOnline, " +
            "p.point = :point, p.effect = :effect, " +
            "p.spaceship = :spaceship, " +
            "p.taskDaily = :taskDaily, " +
            "p.recharge = :recharge, " +
            "p.magicBean = :magicBean, " +
            "p.npcTree = :npcTree, " +
            "p.friend = :friend, " +
            "p.enemy = :enemy, " +
            "p.achievement = :achievement, " +
            "p.missionWeek = :missionWeek, " +
            "p.missionDaily = :missionDaily, " +
            "p.missionRecharge = :missionRecharge, " +
            "p.missionEvent = :missionEvent, " +
            "p.event = :event, " +
            "p.onlineInfo = :onlineInfo, " +
            "p.intrinsic = :intrinsic, " +
            "p.pro = :pro, " +
            "p.trainingOffline = :trainingOffline " +
            "WHERE p.id = :id")
    void saveData(Integer id, String baseInfo, String powerInfo, Integer diamond, Long xu, Integer ruby, Long xuKhoa,
                  String clanInfo, String itemsBag, String itemsBox, String itemsBody, String itemsPet, String itemsOther,
                  String taskInfo, String skillsInfo, String keysSkill, String mapInfo, String barrackInfo,
                  Timestamp resetTime, Timestamp logoutTime, Boolean isOnline, String point,
                  String effect, Integer spaceship, String taskDaily, String recharge, String magicBean, String npcTree,
                  String friend, String enemy, String achievement, String missionWeek,
                  String missionDaily, String missionRecharge, String missionEvent, String event, String onlineInfo,
                  String intrinsic, Long pro, String trainingOffline);
}
