package com.soterocra.aulapds1.services;

import com.soterocra.aulapds1.dto.CredentialsDTO;
import com.soterocra.aulapds1.dto.TokenDTO;
import com.soterocra.aulapds1.entities.Order;
import com.soterocra.aulapds1.entities.User;
import com.soterocra.aulapds1.repositories.UserRepository;
import com.soterocra.aulapds1.security.JWTUtil;
import com.soterocra.aulapds1.services.exceptions.JWTAuthenticationException;
import com.soterocra.aulapds1.services.exceptions.JWTAuthorizationException;
import com.soterocra.aulapds1.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public TokenDTO authenticate(CredentialsDTO dto) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
            authenticationManager.authenticate(authToken);
            String token = jwtUtil.generateToken(dto.getEmail());
            return new TokenDTO(dto.getEmail(), token);
        } catch (AuthenticationException e) {
            throw new JWTAuthenticationException("Bad credentials");
        }
    }

    public User authenticated() {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userRepository.findByEmail(userDetails.getUsername());
        } catch (Exception e) {
            throw new JWTAuthorizationException("Access denied");
        }
    }

    public void validateSelfOrAdmin(Long userId) {
        User user = authenticated();
        if (user == null || (!user.getId().equals(userId) && !user.hasRole( "ROLE_ADMIN"))) {
            throw new JWTAuthorizationException("Access denied");
        }
    }

    public void validateOwnOrderOrAdmin(Order order) {
        User user = authenticated();
        if (user == null || (!user.getId().equals(order.getClient().getId()) && !user.hasRole( "ROLE_ADMIN"))) {
            throw new JWTAuthorizationException("Access denied");
        }
    }

    public TokenDTO refreshToken() {
        User user = authenticated();
        return new TokenDTO(user.getEmail(), jwtUtil.generateToken(user.getEmail()));
    }

    @Transactional
    public void sendNewPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("Email not found");
        }

        String newPass = newPassword();
        user.setPassword(passwordEncoder.encode(newPass));

        userRepository.save(user);

        LOG.info("New password: " + newPass);

    }

    private String newPassword() {
        char[] vect = new char[10];
        for (int i = 0; i < 10; i++) {
            vect[i] = randomChar();
        }

        return new String(vect);
    }

    private char randomChar() {
        Random random = new Random();
        int opt = random.nextInt(3);
        if (opt == 0) { // generate digit
            return (char) (random.nextInt(10) + 48);
        } else if (opt == 1) { // generate uppercase letter
            return (char) (random.nextInt(26) + 65);
        } else { // generate lowercase letter
            return (char) (random.nextInt(26) + 97);
        }
    }

    private boolean hasRole(User user, String roleName) {
        UserDetails userDetails = (UserDetails) user;
        List<String> list = userDetails.getAuthorities().stream().map(role -> role.getAuthority()).collect(Collectors.toList());
        return list.contains(roleName);
    }
}