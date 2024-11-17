package com.pt.authms.model.dtos;

public class UserLoginDTO {
	
	private Long id;

    private String password;

    private String email;

	public UserLoginDTO() {
	}

	/**
	 * @param id
	 * @param password
	 * @param email
	 */
	public UserLoginDTO(Long id, String password, String email) {
		super();
		this.id = id;
		this.password = password;
		this.email = email;
	}
	

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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

