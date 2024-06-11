package com.csci3130.group04.Daltweets.model;
import jakarta.persistence.*;

@Entity
public class LogIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    User userId;

    public LogIn() {
    }

    public LogIn(int id, String username, String password, User userId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userId = userId;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

}
