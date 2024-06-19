package com.csci3130.group04.Daltweets.model;

import jakarta.persistence.*;

@Entity
public class Followers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    User userID;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followerId")
    User followersID;

    public Followers(int id, User userID, User followersID) {
        this.id = id;
        this.userID = userID;
        this.followersID = followersID;
    }

    public Followers() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    public User getFollowersID() {
        return followersID;
    }

    public void setFollowersID(User followersID) {
        this.followersID = followersID;
    }
}
