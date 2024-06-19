package com.csci3130.group04.Daltweets.model;

import jakarta.persistence.*;

@Entity
public class PostLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    public PostLikes(){
    }

    public PostLikes(int id, User user, Post post){
        this.id = id;
        this.user = user;
        this.post = post;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
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
