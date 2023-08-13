package chessGamesDatabase.service;

import chessGamesDatabase.dao.UserRepository;
import chessGamesDatabase.entity.User;
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
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(long id) {
        Optional<User> foundUser = userRepository.findById(id);

        if (foundUser.isEmpty()) {
            throw new RuntimeException("User not found for id: " + id);
        }

        return foundUser.get();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findUsersWithoutRole(String roleName) {
        return userRepository.findUsersWithoutAdminRole(roleName);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}
