package com.csci3130.group04.Daltweets.model;

import jakarta.persistence.*;

@Entity
public class PostTopic{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="postId")
  private Post post;

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="topicId")
  private Topic topic;

  public PostTopic(){
  }

  public PostTopic(int id, Topic topic, Post post){
    this.id = id;
    this.topic = topic;
    this.post = post;
  }

  public int getID(){
    return this.id;
  }

  public Post getPost(){
    return this.post;
  }

  public void setPost(Post post){
    this.post = post;
  }

  public Topic getTopic(){
    return this.topic;
  } 

  public void setTopic(Topic topic){
    this.topic = topic;
  }
}


