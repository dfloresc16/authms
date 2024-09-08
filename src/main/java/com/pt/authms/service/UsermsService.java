package com.pt.authms.service;

import com.pt.authms.model.dtos.UserDTO;
import com.pt.authms.model.dtos.response.UserDTOResponse;

public interface UsermsService {
	public UserDTOResponse save(UserDTO userdto);
}
