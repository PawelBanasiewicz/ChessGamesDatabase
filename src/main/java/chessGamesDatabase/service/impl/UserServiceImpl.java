package chessGamesDatabase.service.impl;

import chessGamesDatabase.dao.UserRepository;
import chessGamesDatabase.entity.User;
import chessGamesDatabase.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public Page<User> findUsersWithoutRoleWithFiltersPageable(String roleName, String usernameFilter, String emailFilter, Boolean enabledFilter,
                                                              LocalDateTime createdDateFromFilter, LocalDateTime createdDateToFilter,
                                                              LocalDateTime lastTimeLoginDateFromFilter,
                                                              LocalDateTime lastTimeLoginDateToFilter, Pageable pageable) {
        return userRepository.findUsersWithoutRoleWithFiltersPageable(roleName, usernameFilter, emailFilter, enabledFilter,
                createdDateFromFilter, createdDateToFilter, lastTimeLoginDateFromFilter, lastTimeLoginDateToFilter, pageable);
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(long userId) {
        userRepository.deleteById(userId);
    }
}
