package chessGamesDatabase.service;

import chessGamesDatabase.entity.User;

import java.util.List;

public interface UserService {

    User findUserById(long userId);

    User findUserByUsername(String username);

    List<User> findUsersWithoutRole(String roleName);

    void saveUser(User user);

    void deleteUserById(long userId);
}
