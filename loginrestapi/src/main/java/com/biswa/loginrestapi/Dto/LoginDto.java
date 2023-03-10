package com.biswa.loginrestapi.Dto;

public class LoginDto {
	private String user_name;
    private String password;
    private String email;

	public String getUser_name() {
		return user_name;
	}
	public void setUsername(String username) {
		this.user_name = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "LoginDto [user_name=" + user_name + ", password=" + password + ", email=" + email + "]";
	}
	
}
