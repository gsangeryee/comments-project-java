package com.saneryee.messageboard.repository;

import java.util.List;
import java.util.Optional;

import com.saneryee.messageboard.models.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Find comments by ParentId
    List<Comment> findByParentId(Long parentId);
    
    // Find comment by Id
    Optional<Comment> findById(Long id);
    

    // Find all parent comments by order by create time desc
    @Query("select c from Comment c where c.parentId = 0 order by c.createdTime desc")
    List<Comment> findAllParentCommentsOrderbyCreatedTimeDesc();


}
    
