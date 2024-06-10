package com.csci3130.group04.Daltweets.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    private String text;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dateCreated;

    private boolean isDeleted;
    private boolean isEdited;

    public Post(){
    }

    public Post(int id, User user, String text, LocalDateTime dateCreated, boolean isDeleted, boolean isEdited){
        this.postID = id;
        this.user = user;
        this.text = text;
        this.dateCreated = dateCreated;
        this.isDeleted = isDeleted;
        this.isEdited = isEdited;
    }

    public int getPostID(){
        return postID;
    }

    public void setPostID(int id){
        this.postID = id;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public LocalDateTime getDateCreated(){
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime date){
        this.dateCreated = date;
    }

    public boolean isDeleted(){
        return isDeleted;
    }

    public void setDeleted(boolean deleted){
        this.isDeleted = deleted;
    }

    public boolean isEdited(){
        return isEdited;
    }

    public void setEdited(boolean edited){
        this.isEdited = edited;
    }
}
