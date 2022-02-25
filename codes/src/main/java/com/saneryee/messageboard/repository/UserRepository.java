package com.saneryee.messageboard.repository;
import java.util.Optional;
import com.saneryee.messageboard.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	@Query("from User where email = :username or username = :username")
	Optional<User> findByUsernameOrEmail(@Param("username") String username);
}
