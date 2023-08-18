package chessGamesDatabase.dao;

import chessGamesDatabase.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT DISTINCT u FROM User u WHERE NOT EXISTS (SELECT r FROM u.roles r WHERE r.roleName = :roleName)")
    List<User> findUsersWithoutAdminRole(@Param("roleName") String roleName);
}
