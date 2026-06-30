package com.saas.support.user.service;

import com.saas.support.common.exception.ResourceNotFoundException;
import com.saas.support.common.exception.BusinessException;
import com.saas.support.user.entity.User;
import com.saas.support.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with email: " + email));
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User", id.toString()));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with email: " + email));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User createUser(String email, String password,
                           String firstName, String lastName) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("Email already in use: " + email);
        }

        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .firstName(firstName)
                .lastName(lastName)
                .isActive(true)
                .isEmailVerified(false)
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public void deactivateUser(UUID id) {
        User user = findById(id);
        user.setActive(false);
        userRepository.save(user);
        log.info("User deactivated: {}", id);
    }
}