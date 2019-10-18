package com.soterocra.aulapds1.resources;

import com.soterocra.aulapds1.dto.CredentialsDTO;
import com.soterocra.aulapds1.dto.TokenDTO;
import com.soterocra.aulapds1.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

    @Autowired
    private AuthService service;

    @PostMapping(value = "/login")
    public ResponseEntity<TokenDTO> login(@RequestBody CredentialsDTO dto) {
        TokenDTO tokenDTO = service.authenticate(dto);
        return ResponseEntity.ok().body(tokenDTO);
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<TokenDTO> refresh() {
        TokenDTO tokenDTO = service.refreshToken();
        return ResponseEntity.ok().body(tokenDTO);
    }

}
