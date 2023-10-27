package chessGamesDatabase.service;

import chessGamesDatabase.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface UserService {

    User findUserById(long userId);

    User findUserByUsername(String username);

    Page<User> findUsersWithoutRoleWithFiltersPageable(String roleName, String usernameFilter, String emailFilter,
                                                       Boolean enabledFilter, LocalDateTime createdDateFromFilter,
                                                       LocalDateTime createdDateToFilter, LocalDateTime lastTimeLoginDateFromFilter,
                                                       LocalDateTime lastTimeLoginDateToFilter, Pageable pageable);

    void saveUser(User user);

    void deleteUserById(long userId);
}
