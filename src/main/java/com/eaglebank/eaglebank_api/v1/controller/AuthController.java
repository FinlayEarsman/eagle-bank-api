package com.eaglebank.eaglebank_api.v1.controller;

import com.eaglebank.eaglebank_api.v1.model.AuthRequest;
import com.eaglebank.eaglebank_api.v1.model.AuthResponse;
import com.eaglebank.eaglebank_api.v1.service.impl.UserDetailsServiceImpl;
import com.eaglebank.eaglebank_api.v1.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        try {
            final UserDetails user = userService.loadUserByUsername(request.getUsername());
            final String jwt = jwtUtil.generateToken(user);
            return ResponseEntity.ok(new AuthResponse(jwt));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(401).body(new AuthResponse("Authentication failed: " + e.getMessage()));
        }
    }
}
