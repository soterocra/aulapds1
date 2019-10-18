package com.soterocra.aulapds1.services;

import com.soterocra.aulapds1.dto.CredentialsDTO;
import com.soterocra.aulapds1.dto.TokenDTO;
import com.soterocra.aulapds1.entities.Order;
import com.soterocra.aulapds1.entities.User;
import com.soterocra.aulapds1.repositories.UserRepository;
import com.soterocra.aulapds1.security.JWTUtil;
import com.soterocra.aulapds1.services.exceptions.JWTAuthenticationException;
import com.soterocra.aulapds1.services.exceptions.JWTAuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

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

    private boolean hasRole(User user, String roleName) {
        UserDetails userDetails = (UserDetails) user;
        List<String> list = userDetails.getAuthorities().stream().map(role -> role.getAuthority()).collect(Collectors.toList());
        return list.contains(roleName);
    }
}