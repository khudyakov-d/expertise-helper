package ru.nsu.ccfit.khudyakov.expertise_helper.features.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean isExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public void create(String email, String name) {
        if (!userRepository.existsByEmail(email)) {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            userRepository.save(user);
        }
    }

    public void update(String email, String name) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(IllegalStateException::new);
        user.setName(name);
        userRepository.save(user);
    }

    public void updateMailPassword(User user, String password) {
        user.setMailPassword(password);
        userRepository.save(user);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(IllegalStateException::new);
    }



}
