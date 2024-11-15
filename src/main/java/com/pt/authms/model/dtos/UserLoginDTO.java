package com.pt.authms.model.dtos;

public class UserLoginDTO {

    private String password;

    private String email;

	public UserLoginDTO() {
	}

	public UserLoginDTO(String password, String email) {
		super();
		this.password = password;
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
    
    
}

