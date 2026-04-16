package com.beemobi.rongthanonline.repository;

import com.beemobi.rongthanonline.data.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Integer> {
    List<UserData> findByUsername(String username);

    List<UserData> findByUsernameAndPassword(String username, String password);
}
