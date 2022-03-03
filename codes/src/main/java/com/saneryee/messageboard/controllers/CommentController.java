package com.saneryee.messageboard.controllers;

import java.util.ArrayList;
import java.util.List;

import com.saneryee.messageboard.models.Comment;
import com.saneryee.messageboard.repository.CommentRepository;
import com.saneryee.messageboard.security.services.CommentSeviceImpl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comments")
public class CommentController {
  @Autowired
  CommentRepository commentRepository;
   
  @Autowired
  CommentSeviceImpl commentSeviceImpl;

  @GetMapping("/all")
  public ResponseEntity<List<Comment>> getAllComments(@RequestParam(required = false) Long commentId) {
    try{
      List<Comment> comments = new ArrayList<Comment>();

      if(commentId == null)
        commentRepository.findAll().forEach(comments::add); // Lambda function
      else
        commentRepository.findById(commentId);

      if(comments.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(comments, HttpStatus.OK);
    } catch (Exception e){
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  @GetMapping("/tree")
  public ResponseEntity<List<Comment>>getCommentsTree(@RequestParam(required = false) Long commentId) {
    try{
      List<Comment> comments = new ArrayList<Comment>();

      if(commentId == null)
        comments = commentSeviceImpl.getAllComments();
      else
        commentRepository.findByParentId(commentId).forEach(comments::add);

      if(comments.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      
      return new ResponseEntity<>(comments, HttpStatus.OK);
    }catch (Exception e){
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  
  @PostMapping("/new")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
    try {
      Comment _comment = commentRepository
              .save(new Comment(comment.getUsername(), comment.getComment(), comment.getParentId())
      );
      return new ResponseEntity<>(_comment, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
    }
  }


  // @EnableGlobalMethodSecurity(prePostEnabled = true) in WebSecurityConfig class
  // Using @PreAuthorize annotation to restrict access to the method
  @GetMapping("/user")
  @PreAuthorize("hasRole('USER')") // This means only user have access to this request
  public String userAccess() {
    return "User Login Content.";
  }
}
