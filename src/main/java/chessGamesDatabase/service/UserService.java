package chessGamesDatabase.service;

import chessGamesDatabase.entity.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(long id);

    User findByUsername(String username);

    List<User> findUsersWithoutRole(String roleName);

    void save(User user);

    void deleteById(long id);
}
