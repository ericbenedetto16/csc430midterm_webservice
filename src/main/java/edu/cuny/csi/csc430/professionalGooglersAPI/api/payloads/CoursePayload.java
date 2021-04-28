package edu.cuny.csi.csc430.professionalGooglersAPI.api.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Date;

public class CoursePayload {
	private String name;
	private Integer teacher;
	private Date endDate;
	
	@JsonCreator
	public CoursePayload(String name, Integer teacher, Date endDate) {
		this.name = name;
		this.teacher = teacher;
		this.endDate = endDate;
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getTeacher() {
		return teacher;
	}

	public Date getEndDate() {
		return endDate;
	}
}
