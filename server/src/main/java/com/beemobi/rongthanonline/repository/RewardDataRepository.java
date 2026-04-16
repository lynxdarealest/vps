package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.RewardData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Transactional
@Repository
public interface RewardDataRepository extends JpaRepository<RewardData, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM rto_reward WHERE player_id = ? AND reward_time IS NULL")
    List<RewardData> findByPlayerId(Integer playerId);

    @Modifying
    @Query("UPDATE RewardData r SET r.rewardTime = :rewardTime WHERE r.id = :id")
    void setRewardTime(Long id, Timestamp rewardTime);
}
