package com.pt.authms.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pt.authms.model.commons.CommonController;
import com.pt.authms.model.dtos.GenericResponseDTO;
import com.pt.authms.model.dtos.TokenDTO;
import com.pt.authms.model.dtos.UserDTO;
import com.pt.authms.model.dtos.UserLoginDTO;
import com.pt.authms.model.dtos.response.UserDTOResponse;
import com.pt.authms.service.impl.AuthServiceImpl;
import com.pt.authms.service.impl.NotificationServiceImpl;
import com.pt.authms.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/auth/")
public class AuthController extends CommonController{
    
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private UserServiceImpl userService;
    
    @Autowired
    private NotificationServiceImpl notificationServiceImpl;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<GenericResponseDTO<TokenDTO>> login(@RequestBody UserLoginDTO userDTO) {
        try {
            logger.info(String.format("Start login for user with email: %s", userDTO.getEmail()));
            TokenDTO tokenDTO = authService.login(userDTO);
            return ResponseEntity.ok(new GenericResponseDTO(SUCCESS, HttpStatus.OK.value(),null,null,
                    "service execute succesfully",tokenDTO));
        }catch (ResponseStatusException e){
            logger.error("Exception: " + e.getMessage());
            return new ResponseEntity<>(new GenericResponseDTO<>(ERROR, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.toString(),
                    e.getMessage(), "service execute", null), HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            logger.error("Exception: " + e.getMessage());
            return new ResponseEntity<>(new GenericResponseDTO<>(ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                    e.getMessage(), "service execute", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ResponseEntity<GenericResponseDTO<UserDTO>> create(@RequestBody UserDTO userDTO) {
        try {
            logger.info(String.format("Start create user: %s", userDTO.getUserName()));
            UserDTOResponse response = userService.save(userDTO);
            notificationServiceImpl.sendNotification(response);
            return ResponseEntity.ok(new GenericResponseDTO(SUCCESS, HttpStatus.OK.value(),null,null,
                    "service execute succesfully",response));
        }catch (ResponseStatusException e){
            logger.error("Exception: " + e.getMessage());
            return new ResponseEntity<>(new GenericResponseDTO<>(ERROR, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.toString(),
                    e.getMessage(), "service execute", null), HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            logger.error("Exception: " + e.getMessage());
            return new ResponseEntity<>(new GenericResponseDTO<>(ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                    e.getMessage(), "service execute", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/validate", method = RequestMethod.POST)
    public ResponseEntity<GenericResponseDTO<TokenDTO>> validate(@RequestParam String token){
        try {
            logger.info("<<<Start validate from AuthController>>>");
            TokenDTO tokenDTO = authService.validate(token);
            return ResponseEntity.ok(new GenericResponseDTO(SUCCESS,HttpStatus.OK.value(), null,null,
                    "service execute succesfully",tokenDTO));
        }catch (ResponseStatusException e){
            logger.error("Exception: " + e.getMessage());
            return new ResponseEntity<>(new GenericResponseDTO<>(ERROR, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.toString(),
                    e.getMessage(), "service execute", null), HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            logger.error("Exception: " + e.getMessage());
            return new ResponseEntity<>(new GenericResponseDTO<>(ERROR, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.toString(),
                    e.getMessage(), "service execute", null), HttpStatus.UNAUTHORIZED);
        }
    }
    
    @RequestMapping(path = "/active", method = RequestMethod.GET)
    public ResponseEntity<GenericResponseDTO<Boolean>> activeUser(@RequestParam String pin){
        try {
            logger.info("<<<Start active user from AuthController>>>");
            return ResponseEntity.ok(new GenericResponseDTO(SUCCESS,HttpStatus.OK.value(), null,null,
                    "service execute succesfully",notificationServiceImpl.validatePin(pin)));
        }catch (ResponseStatusException e){
            logger.error("Exception: " + e.getMessage());
            return new ResponseEntity<>(new GenericResponseDTO<>(ERROR, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.toString(),
                    e.getMessage(), "service execute", null), HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            logger.error("Exception: " + e.getMessage());
            return new ResponseEntity<>(new GenericResponseDTO<>(ERROR, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.toString(),
                    e.getMessage(), "service execute", null), HttpStatus.UNAUTHORIZED);
        }
    }
    

}
