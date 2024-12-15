package com.pt.authms.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.pt.authms.config.JwtProvider;
import com.pt.authms.model.dtos.TokenDTO;
import com.pt.authms.model.dtos.UserDTO;
import com.pt.authms.model.dtos.UserLoginDTO;
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
    
    
	private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);


    /**
     * @param userDTO
     * @return
     */
    @Override
    public TokenDTO login(UserLoginDTO userDTO) {
    	log.info(String.format("[%s] User with email [%s] ", this.getClass().getSimpleName(),userDTO.getEmail()));
        Userms user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        if (encoder.matches(userDTO.getPassword(), user.getPassword())) {
        	log.info(String.format("[%s] Login successful [%s]", this.getClass().getSimpleName(),userDTO.getEmail()));
            return new TokenDTO(jwtProvider.createToken(user));
        } else {
        	log.info(String.format("[%s] Password not match [%s] ", this.getClass().getSimpleName(),userDTO.getEmail()));
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * @param token
     * @return
     */
    @Override
    public TokenDTO validate(String token) {
    	log.info(String.format("[%s] Validate token ", this.getClass().getSimpleName()));
        jwtProvider.validate(token);
        log.info(String.format("[%s] Token is valid ", this.getClass().getSimpleName()));
        String userName = jwtProvider.getUserNameFromToken(token);
        log.info(String.format("[%s] Token from user [%s] ", this.getClass().getSimpleName(), userName));
        Userms user = userRepository.findByUserName(userName)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        log.info(String.format("[%s] Change Token", this.getClass().getSimpleName()));
        return new TokenDTO(token);
    }

}
