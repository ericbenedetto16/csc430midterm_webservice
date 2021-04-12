package edu.cuny.csi.csc430.professionalGooglersAPI.api.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;

public class UserPayload {
	private String firstName, lastName, email, password;
	
	@JsonCreator
	public UserPayload(String firstName, String lastName, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Boolean isValid() {
		return firstName != null && lastName != null && email != null && password != null;
	}
	
	public Boolean isValidEdit() {
		return firstName != null && lastName != null && email != null;
	}
}
