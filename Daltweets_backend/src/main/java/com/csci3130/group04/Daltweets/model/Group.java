
package com.csci3130.group04.Daltweets.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Group{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int groupID ;
    private String name;
    private LocalDateTime dateCreated;
    private boolean isDeleted;
    private boolean isPublic;

    public Group() {
    }

    public Group(int id, String name, LocalDateTime dateCreated, boolean isPublic) {
        this.groupID = id;
        this.name = name;
        this.dateCreated = dateCreated;
        this.isDeleted = false;
        this.isPublic = isPublic;
    }

    public int getId() {
        return groupID;
    }

    public void setId(int id) {
        this.groupID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted (boolean isDeleted) {
      this.isDeleted = isDeleted;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic (boolean isPublic) {
      this.isPublic = isPublic;
    }

}
