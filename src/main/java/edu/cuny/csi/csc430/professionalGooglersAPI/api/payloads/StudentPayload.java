package edu.cuny.csi.csc430.professionalGooglersAPI.api.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;

public class StudentPayload {
	private UserPayload student, parent;
	private Integer grade;
	
	@JsonCreator
	public StudentPayload(UserPayload student, UserPayload parent, Integer grade) {
		this.student = student;
		this.parent = parent;
		this.grade = grade;
	}

	public Integer getGrade() {
		return grade;
	}
	
	public UserPayload getStudent() {
		return student;
	}
	
	public UserPayload getParent() {
		return parent;
	}
	
	public Boolean isValid() {
		return student.isValid() && parent.isValid() && grade != null;
	}
	
	public Boolean isValidEdit() {
		return student.isValidEdit() && parent.isValidEdit() && grade != null;
	}
}
