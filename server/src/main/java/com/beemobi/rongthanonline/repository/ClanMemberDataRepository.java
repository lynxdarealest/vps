package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.ClanMemberData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface ClanMemberDataRepository extends JpaRepository<ClanMemberData, Long> {
    List<ClanMemberData> findByServer(Integer server);

    @Modifying
    @Query("UPDATE ClanMemberData m SET m.role = :role WHERE m.id = :id")
    void setRole(Long id, Integer role);

    @Modifying
    @Query("DELETE FROM ClanMemberData m WHERE m.id = :id")
    void deleteClanMemberById(Long id);

    @Modifying
    @Query("UPDATE ClanMemberData m SET m.name = :name, m.role = :role, m.power = :power, m.point = :point WHERE m.id = :id")
    void saveData(Long id, String name, Integer role, Long power, String point);
}
