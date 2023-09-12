package chessGamesDatabase.service.impl;

import chessGamesDatabase.dao.UserRepository;
import chessGamesDatabase.entity.User;
import chessGamesDatabase.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findUserById(long userId) {
        Optional<User> foundUser = userRepository.findById(userId);

        if (foundUser.isEmpty()) {
            throw new RuntimeException("User not found for userId: " + userId);
        }

        return foundUser.get();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public List<User> findUsersWithoutRole(String roleName) {
        return userRepository.findUsersWithoutAdminRole(roleName);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(long userId) {
        userRepository.deleteById(userId);
    }
}
