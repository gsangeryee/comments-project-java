package com.saneryee.messageboard.models;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "comment")
    private String comment;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "username")
    private String username;

    @CreationTimestamp
    @Column(name = "created_time")
    private Date createdTime;
    /**
     * replies
     */
    @Transient
    private List<Comment> children;

    public Comment() {
    }

    public Comment(String username, String comment, Long parentId) {
        this.comment = comment;
        this.parentId = parentId;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public List<Comment> getChildren() {
        return children;
    }

    public void setChildren(List<Comment> children) {
        this.children = children;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", parentId=" + parentId +
                ", username='" + username + '\'' +
                ", createdTime=" + createdTime +
                '}';
    }    
}
