package edu.cuny.csi.csc430.professionalGooglersAPI.api.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;

public class FacultyPayload extends UserPayload {
	private Double salary;
	private Boolean active = true;
	
	@JsonCreator
	public FacultyPayload(String firstName, String lastName, String email, String password, Double salary) {
		super(firstName, lastName, email, password);
		this.salary = salary;
	}
	
	@JsonCreator
	public FacultyPayload(String firstName, String lastName, String email, Double salary, Boolean active) {
		super(firstName, lastName, email);
		this.salary = salary;
		this.active = active;
	}
	
	public Double getSalary() {
		return salary;
	}
	
	public Boolean getActive() {
		return active;
	}
	
	public Boolean isValid() {
		return super.getFirstName() != null && super.getLastName() != null && super.getEmail() != null && super.getPassword() != null && salary != null;
	}
	
	public Boolean isValidEdit() {
		return super.getFirstName() != null && super.getLastName() != null && super.getEmail() != null && salary != null && active != null;
	}
}
