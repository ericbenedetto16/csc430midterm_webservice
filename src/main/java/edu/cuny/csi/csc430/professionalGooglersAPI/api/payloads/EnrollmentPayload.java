package edu.cuny.csi.csc430.professionalGooglersAPI.api.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;

public class EnrollmentPayload {
	private Integer course, grade;
	
	@JsonCreator
	public EnrollmentPayload(Integer course) {
		this.course = course;
	}
	
	@JsonCreator
	public EnrollmentPayload(Integer course, Integer grade) {
		this.course = course;
		this.grade = grade;
	}
	
	public Integer getCourseId() {
		return course;
	}
	
	public Integer getGrade() {
		return grade;
	}
	
	public Boolean isValid() {
		return course != null;
	}
	
	public Boolean isValidGrade() {
		return course != null && grade != null;
	}
}
