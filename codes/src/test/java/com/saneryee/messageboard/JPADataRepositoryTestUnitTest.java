package com.saneryee.messageboard;

import com.saneryee.messageboard.models.Comment;
import com.saneryee.messageboard.models.User;
import com.saneryee.messageboard.repository.CommentRepository;
import com.saneryee.messageboard.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class JPADataRepositoryTestUnitTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;

    @Test
    public void should_find_no_users_if_repository_is_empty() {
        Iterable<User> users = userRepository.findAll();
        assertThat(users).isEmpty();
    }

    // Test CommentRepository
    @Test
    public void should_store_a_comment() {
        Comment comment = commentRepository
                .save(new Comment("alan","This is a alan test comment", 0L));
        assertThat(comment).hasFieldOrPropertyWithValue("username","alan");
        assertThat(comment).hasFieldOrPropertyWithValue("comment","This is a alan test comment");
        assertThat(comment).hasFieldOrPropertyWithValue("parentId", 0L);
    }

    @Test
    public void should_find_all_comments() {
        Comment comment1 = new Comment("alan","This is a alan test comment", 0L);
        entityManager.persist(comment1);
        Comment comment2 = new Comment("Bob","This is a Bon test comment", 1L);
        entityManager.persist(comment2);
        Comment comment3 = new Comment("Cindy","This is a Cindy test comment", 0L);
        entityManager.persist(comment3);
        Iterable<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(3).contains(comment1,comment2,comment3);
    }

    @Test
    public void should_find_all_comments_by_parent_comments_order_by_createdtime_desc() {
        Comment comment1 = new Comment("alan","This is a alan test comment", 0L);
        entityManager.persist(comment1);
        Comment comment2 = new Comment("Bob","This is a Bon test comment", 0L);
        entityManager.persist(comment2);
        Comment comment3 = new Comment("Cindy","This is a Cindy Rely Alan comment", comment1.getId());
        entityManager.persist(comment3);
        Comment comment4 = new Comment("David","This is a David Reply Alan comment", comment1.getId());
        entityManager.persist(comment4);
        Iterable<Comment> comments = commentRepository.findAllParentCommentsOrderbyCreatedTimeDesc();
        assertThat(comments).hasSize(2).contains(comment1,comment2);
    }


}
