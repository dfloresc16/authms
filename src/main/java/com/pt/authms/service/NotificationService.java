package com.pt.authms.service;

import java.io.IOException;

import com.pt.authms.model.dtos.UserDTO;
import com.pt.authms.model.dtos.response.UserDTOResponse;

import jakarta.mail.MessagingException;

public interface NotificationService {
	
	public void sendNotification(UserDTOResponse userDTO) throws MessagingException, IOException;
	
	public boolean validatePin(String pin);
}
