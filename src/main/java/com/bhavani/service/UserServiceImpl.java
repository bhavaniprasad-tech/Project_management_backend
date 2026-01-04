package com.bhavani.service;

import com.bhavani.model.User;
import com.bhavani.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // ✅ FIX: use SecurityContext instead of JWT parsing
    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {

        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findAllByEmail(email);
        if (user == null) {
            throw new Exception("User not found");
        }
        return user;
    }

    @Override
    public User findUserById(Long userId) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new Exception("User not found");
        }
        return optionalUser.get();
    }

    @Override
    public User updateUsersProjectSize(User user, int number) {
        user.setProjectSize(user.getProjectSize() + number);
        return userRepository.save(user);
    }
}
