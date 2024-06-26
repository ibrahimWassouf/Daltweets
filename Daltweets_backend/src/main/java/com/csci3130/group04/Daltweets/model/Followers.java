package com.csci3130.group04.Daltweets.model;

import jakarta.persistence.*;

@Entity
public class Followers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "userId")
    User user;
    @ManyToOne
    @JoinColumn(name = "followerId")
    User follower;

    public enum Status{
        PENDING,
        ACCEPTED
    }
    
    @Enumerated(EnumType.STRING)
    private Status status;

    public Followers(int id, User user, User follower, Status status) {
        this.id = id;
        this.user = user;
        this.follower = follower;
        this.status = status;
    }

    public Followers() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User userID) {
        this.user = userID;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User followersID) {
        this.follower = followersID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
