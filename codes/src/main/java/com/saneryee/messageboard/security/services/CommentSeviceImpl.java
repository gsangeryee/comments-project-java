package com.saneryee.messageboard.security.services;

import java.util.ArrayList;
import java.util.List;

import com.saneryee.messageboard.models.Comment;
import com.saneryee.messageboard.repository.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentSeviceImpl {
    @Autowired
    CommentRepository commentRepository;

    // Get this Comment's all children node.
    public List<Comment> getAllComments(){
        List<Comment> comments = new ArrayList<Comment>();
        commentRepository.findAllParentCommentsOrderbyCreatedTimeDesc().forEach(
            comment -> {
                    // Parent node
                    if(getChildren(comment).size() > 0){
                        comment.setChildren(getChildren(comment));
                    }
                    comments.add(comment);
                }
        );
        return comments;
    }

    
    public List<Comment> getChildren(Comment comment){
        List<Comment> comments = new ArrayList<Comment>();

        // Get all children of this comment.
        commentRepository.findByParentId(comment.getId()).forEach(
            child -> {
                if(getChildren(child).size() > 0){
                    child.setChildren(getChildren(child));
                }
                comments.add(child);
        });
        return comments;
    }

}
