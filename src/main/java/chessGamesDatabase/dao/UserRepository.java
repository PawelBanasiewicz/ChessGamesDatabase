package chessGamesDatabase.dao;

import chessGamesDatabase.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);

    @Query("SELECT DISTINCT u FROM User u " +
            "WHERE NOT EXISTS (SELECT r FROM u.roles r WHERE r.roleName = :roleNameFilter) " +
            "AND (:usernameFilter IS NULL OR u.username LIKE %:usernameFilter%) " +
            "AND (:emailFilter IS NULL OR u.email LIKE %:emailFilter%) " +
            "AND (:enabledFilter IS NULL OR u.enabled = :enabledFilter) " +
            "AND (:createdDateFromFilter IS NULL OR u.createdAt >= :createdDateFromFilter) " +
            "AND (:createdDateToFilter IS NULL OR u.createdAt <= :createdDateToFilter) " +
            "AND (:lastTimeLoginDateFromFilter IS NULL OR u.lastLogin >= :lastTimeLoginDateFromFilter) " +
            "AND (:lastTimeLoginDateToFilter IS NULL OR u.lastLogin <= :lastTimeLoginDateToFilter)"
    )
    Page<User> findUsersWithoutRoleWithFiltersPageable(@Param("roleNameFilter") String roleNameFilter,
                                                       @Param("usernameFilter") String usernameFilter,
                                                       @Param("emailFilter") String emailFilter,
                                                       @Param("enabledFilter") Boolean enableFilter,
                                                       @Param("createdDateFromFilter") LocalDateTime createdDateFromFilter,
                                                       @Param("createdDateToFilter") LocalDateTime createdDateToFilter,
                                                       @Param("lastTimeLoginDateFromFilter") LocalDateTime lastTimeLoginDateFromFilter,
                                                       @Param("lastTimeLoginDateToFilter") LocalDateTime lastTimeLoginDateToFilter,
                                                       Pageable pageable);
}
