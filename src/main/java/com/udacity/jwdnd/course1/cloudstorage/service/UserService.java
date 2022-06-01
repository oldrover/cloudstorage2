package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
public class
UserService {

    //private final UserMapper userMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    HashService hashService;

    public boolean isUsernameAvailable(String username) {
        //return userMapper.getUser(username) == null;
       Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
       return user.isEmpty();
    }

    public User createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userRepository.save(new User(null, user.getUsername(), encodedSalt, hashedPassword, user.getFirstName(), user.getLastName()));
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }
}

