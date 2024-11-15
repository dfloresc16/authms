package com.pt.authms.service;

import com.pt.authms.model.dtos.TokenDTO;
import com.pt.authms.model.dtos.UserDTO;
import com.pt.authms.model.dtos.UserLoginDTO;
import com.pt.authms.model.dtos.response.UserDTOResponse;

public interface AuthService {
	public TokenDTO login(UserLoginDTO userDTO);
	
	public TokenDTO validate(String token);
}
