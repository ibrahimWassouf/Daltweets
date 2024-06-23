package com.csci3130.group04.Daltweets.model;

import jakarta.persistence.*;

@Entity
public class Followers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followerId")
    User follower;

    public Followers(int id, User user, User follower) {
        this.id = id;
        this.user = user;
        this.follower = follower;
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
}
