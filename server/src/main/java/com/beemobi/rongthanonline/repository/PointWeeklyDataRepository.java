package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.PointWeeklyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface PointWeeklyDataRepository extends JpaRepository<PointWeeklyData, Long> {

    Optional<PointWeeklyData> findByPlayerIdAndStartWeek(Integer playerId, LocalDate startWeek);

    List<PointWeeklyData> findByServerAndPlayerIdAndStartWeekAndEventPoint(Integer server, Integer playerId, LocalDate startWeek, Long eventPoint);

    List<PointWeeklyData> findByServerAndPlayerIdAndStartWeekAndEventOtherPoint(Integer server, Integer playerId, LocalDate startWeek, Long eventOtherPoint);

    List<PointWeeklyData> findByServerAndPlayerIdAndStartWeekAndBossPoint(Integer server, Integer playerId, LocalDate startWeek, Long bossPoint);

    List<PointWeeklyData> findByServerAndPlayerIdAndStartWeekAndActivePoint(Integer server, Integer playerId, LocalDate startWeek, Long activePoint);

    @Query(value = """
            SELECT *
            FROM rto_weekly lpw
            WHERE lpw.server = ? AND lpw.start_week = ? AND event_point > 0 AND event_time IS NOT NULL
            ORDER BY event_point DESC, event_time ASC
            LIMIT ?
            """, nativeQuery = true)
    List<PointWeeklyData> findTopEvent(Integer server, LocalDate startWeek, Integer limit);

    @Query(value = """
            SELECT *
            FROM rto_weekly lpw
            WHERE lpw.server = ? AND lpw.start_week = ? AND event_other_point > 0 AND event_other_time IS NOT NULL
            ORDER BY event_other_point DESC, event_other_time ASC
            LIMIT ?
            """, nativeQuery = true)
    List<PointWeeklyData> findTopEventOther(Integer server, LocalDate startWeek, Integer limit);

    @Query(value = """
            SELECT *
            FROM rto_weekly lpw
            WHERE lpw.server = ? AND lpw.start_week = ? AND active_point > 0 AND active_time IS NOT NULL
            ORDER BY active_point DESC, active_time ASC
            LIMIT ?
            """, nativeQuery = true)
    List<PointWeeklyData> findTopActive(Integer server, LocalDate startWeek, Integer limit);

    @Query(value = """
            SELECT *
            FROM rto_weekly lpw
            WHERE lpw.server = ? AND lpw.start_week = ? AND boss_point > 0 AND boss_time IS NOT NULL
            ORDER BY boss_point DESC, boss_time ASC
            LIMIT ?
            """, nativeQuery = true)
    List<PointWeeklyData> findTopBoss(Integer server, LocalDate startWeek, Integer limit);

    @Modifying
    @Query("UPDATE PointWeeklyData p SET " +
            "p.activePoint = :activePoint, " +
            "p.activeTime = :activeTime, " +
            "p.eventPoint = :eventPoint, " +
            "p.eventTime = :eventTime, " +
            "p.bossPoint = :bossPoint, " +
            "p.bossTime = :bossTime, " +
            "p.eventOtherPoint = :eventOtherPoint, " +
            "p.eventOtherTime = :eventOtherTime " +
            "WHERE p.id = :id")
    void saveData(Long id, Long activePoint, Timestamp activeTime, Long eventPoint, Timestamp eventTime,
                  Long bossPoint, Timestamp bossTime, Long eventOtherPoint, Timestamp eventOtherTime);
}
