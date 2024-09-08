package com.pt.authms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.pt.authms.config.JwtProvider;
import com.pt.authms.model.dtos.TokenDTO;
import com.pt.authms.model.dtos.UserDTO;
import com.pt.authms.model.entity.Userms;
import com.pt.authms.repository.UsermsRepository;
import com.pt.authms.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UsermsRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    /**
     * @param userDTO
     * @return
     */
    @Override
    public TokenDTO login(UserDTO userDTO) {
        Userms user = userRepository.findByUserName(userDTO.getUserName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        if (encoder.matches(userDTO.getPassword(), user.getPassword())) {
            return new TokenDTO(jwtProvider.createToken(user));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * @param token
     * @return
     */
    @Override
    public TokenDTO validate(String token) {
        jwtProvider.validate(token);
        String userName = jwtProvider.getUserNameFromToken(token);
        Userms user = userRepository.findByUserName(userName)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        return new TokenDTO(token);
    }

}
