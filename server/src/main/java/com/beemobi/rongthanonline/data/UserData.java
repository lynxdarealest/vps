package com.beemobi.rongthanonline.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "rto_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "server")
    public Integer server;

    @Column(name = "username")
    public String username;

    @Column(name = "password")
    public String password;

    @Column(name = "is_lock")
    public Boolean isLock;

    @Column(name = "lock_time")
    public Timestamp lockTime;
}
