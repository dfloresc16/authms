package com.pt.authms.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
	
	
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
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
        Userms existingUser = userRepository.findByUserName(userDTO.getUserName())
        		.orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "User already exists"));

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
    
    
    public UserDTOResponse getUserInformation(String email) {
    	Userms existsUser = userRepository.findByEmail(email)
    			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
    	log.info(String.format("Information: \n %s", existsUser.toString()));
    	
    	return genericMapper.toDto(existsUser, UserDTOResponse.class);
    }


	@Override
	public Boolean updatePassword(UserDTO userdto) {
		Userms existsUserms = userRepository.findByEmail(userdto.getEmail())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
		log.info(String.format("Information : \n", existsUserms.toString()));
		log.info("new password : " + userdto.getPassword());
		log.info("old password : " + existsUserms.getPassword());
		existsUserms.setPassword(encoder.encode(userdto.getPassword()));
		Userms save = userRepository.save(existsUserms);
		log.info(String.format("Update Password : \n", save.toString()));
		return true;
	}
    
    

}
