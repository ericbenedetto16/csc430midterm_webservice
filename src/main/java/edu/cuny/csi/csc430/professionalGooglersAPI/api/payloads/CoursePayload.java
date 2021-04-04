package edu.cuny.csi.csc430.professionalGooglersAPI.api.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;

public class CoursePayload {
	private String name;
	private Integer teacher;
	
	@JsonCreator
	public CoursePayload(String name, Integer teacher) {
		this.name = name;
		this.teacher = teacher;
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getTeacher() {
		return teacher;
	}
}
