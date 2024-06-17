package com.csci3130.group04.Daltweets.model;
import jakarta.persistence.*;

@Entity
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    User user;

    public Login() {
    }

    public Login(int id, String username, String password, User userId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.user = userId;
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

    public User getUser() {
        return user;
    }

    public void setUser(User userId) {
        this.user = userId;
    }

}
