package com.pt.authms.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.pt.authms.model.dtos.UserDTO;
import com.pt.authms.model.dtos.response.UserDTOResponse;
import com.pt.authms.model.entity.Userms;
import com.pt.authms.repository.UsermsRepository;
import com.pt.authms.service.NotificationService;
import com.pt.authms.utils.PinGenerator;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private UsermsRepository repository;
	
	@Autowired 
	private UserServiceImpl userServiceImpl;
	
	private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);


	@Override
	public void sendNotification(UserDTOResponse userDTO){
		try {
			
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

			// Cargar la plantilla de HTML desde el classpath
			ClassPathResource resource = new ClassPathResource("templates/email-template.html");
			String htmlTemplate;

			try (InputStream inputStream = resource.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
				htmlTemplate = reader.lines().collect(Collectors.joining(System.lineSeparator()));
			}

			log.info(String.format("[%s] PIN : %s", this.getClass().getSimpleName(),userDTO.getPin()));
			
			String htmlContent = htmlTemplate.replace("{{nombre}}", userDTO.getName() + " " + userDTO.getLastName())
					.replace("{{correo}}", userDTO.getEmail()).replace("{{pin}}", userDTO.getPin());

			helper.setTo(userDTO.getEmail().trim());
			helper.setSubject(String.format("Activacion de cuenta para:", userDTO.getName()));
			helper.setText(htmlContent, true);
			helper.setFrom("diegoflowers444@gmail.com");
			log.info(String.format("[%s] Email send for user with email %s", this.getClass().getSimpleName(),userDTO.getEmail()));
			mailSender.send(mimeMessage);
		} catch (Exception e) {
			log.error("Error at sendNotification",e);
		}

	}
	
	
	@Override
	public void sendRecoveryPass(String email){
		try {
			UserDTOResponse userDTO = userServiceImpl.getUserInformation(email);
			String pin = PinGenerator.generatePin();
			log.info("PIN recoveryPass : " + pin);
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

			// Cargar la plantilla de HTML desde el classpath
			ClassPathResource resource = new ClassPathResource("templates/email-template.html");
			String htmlTemplate;

			try (InputStream inputStream = resource.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
				htmlTemplate = reader.lines().collect(Collectors.joining(System.lineSeparator()));
			}

			// Reemplazar los placeholders
			log.info("pin: "+ userDTO.getPin());
			String htmlContent = htmlTemplate.replace("{{nombre}}", userDTO.getName() + " " + userDTO.getLastName())
					.replace("{{correo}}", userDTO.getEmail()).replace("{{pin}}", pin);

			helper.setTo(userDTO.getEmail().trim());
			helper.setSubject(String.format("Recuperacion de cuenta para:", userDTO.getName()));
			helper.setText(htmlContent, true);
			helper.setFrom("diegoflowers444@gmail.com");

			mailSender.send(mimeMessage);
		} catch (Exception e) {
			log.error("e",e);
		}

	}
	
	@Override
	public boolean validatePin(String pin) {
		boolean pinValid = PinGenerator.isPinValid(pin);
		Optional<Userms> userms = repository.findByPin(pin);
		String pinUser = userms.get().getPin();
		log.info(String.format("pinUser = %s", pinUser));
		log.info(String.format("pin = %s", pin));
		if(pinUser.equals(pin)) {
			userms.get().setActive(true);
			repository.save(userms.get());
			PinGenerator.removePin(pinUser);
			return true;
		}
		return false;
	}
	

}
