package com.pt.authms.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.pt.authms.model.dtos.UserDTO;
import com.pt.authms.model.dtos.response.UserDTOResponse;
import com.pt.authms.model.entity.GenericMapper;
import com.pt.authms.model.entity.Userms;
import com.pt.authms.repository.UsermsRepository;
import com.pt.authms.service.UsermsService;
import com.pt.authms.utils.PinGenerator;

@Service
public class UserServiceImpl implements UsermsService{
	
	@Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsermsRepository userRepository;

    @Autowired
    private GenericMapper genericMapper;



    /**
     * @param user
     * @return
     */
    @Override
    public UserDTOResponse save(UserDTO userDTO) {
        Optional<Userms> existingUser = userRepository.findByUserName(userDTO.getUserName());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        Userms newUser = new Userms(
                userDTO.getName(),
                userDTO.getLastName(),
                userDTO.getUserName(),
                userDTO.getPhoneNumber(),
                encoder.encode(userDTO.getPassword()),
                userDTO.getEmail(),
                false,
                PinGenerator.generatePin()
        );

        Userms savedUser = userRepository.save(newUser);
        return genericMapper.toDto(savedUser, UserDTOResponse.class);
    }

}
