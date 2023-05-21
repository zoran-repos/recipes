package com.recipes.services;

import com.recipes.model.User;
import com.recipes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByUserId(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(String name, User updatedUser) {
        Optional<User> userOptional = userRepository.findByUsername(name);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(updatedUser.getUsername());
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with name: " + name);
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
