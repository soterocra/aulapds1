package com.soterocra.aulapds1.services;

import com.soterocra.aulapds1.dto.CredentialsDTO;
import com.soterocra.aulapds1.dto.TokenDTO;
import com.soterocra.aulapds1.security.JWTUtil;
import com.soterocra.aulapds1.services.exceptions.JWTAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Transactional(readOnly = true)
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

}
