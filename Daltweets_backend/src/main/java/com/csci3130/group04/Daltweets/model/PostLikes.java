package com.csci3130.group04.Daltweets.model;

import jakarta.persistence.*;

@Entity
public class PostLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int likeID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postID")
    private Post post;

    public PostLikes(){
    }

    public PostLikes(int id, User user, Post post){
        this.likeID = id;
        this.user = user;
        this.post = post;
    }

    public int getLikeID(){
        return likeID;
    }

    public void setLikeID(int id){
        this.likeID = id;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public Post getPost(){
        return post;
    }

    public void setPost(Post post){
        this.post = post;
    }
}
