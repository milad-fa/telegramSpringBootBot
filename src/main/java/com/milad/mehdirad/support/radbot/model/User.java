package com.milad.mehdirad.support.radbot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "telegram_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "a")
    private long id;
    private long userId;
    private String phone;
    private String username;
    private String name;
    private boolean isBlocked = false;


    public User(long userId, String phone, String username, String name) {
        this.userId = userId;
        this.phone = phone;
        this.username = username;
        this.name = name;
    }

    public User() {
    }
}
