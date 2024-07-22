package com.csci3130.group04.Daltweets.utils;

import java.time.LocalDateTime;

import com.csci3130.group04.Daltweets.model.Post;
import com.fasterxml.jackson.annotation.JsonFormat;


public class PostResponseDTO {
    private int id;

    private String creator;

    private String text;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;

    private int commentCount;
   
    public PostResponseDTO() {
    }
    
    public PostResponseDTO(Post post, int commentCount) {
        this.id = post.getPostID();
        this.creator = post.getUser().getUsername();
        this.text = post.getText();
        this.dateCreated =  post.getDateCreated();
        this.commentCount = commentCount;
        this.commentCount = commentCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    
    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String postCreator) {
        this.creator = postCreator;
    }

    

}
