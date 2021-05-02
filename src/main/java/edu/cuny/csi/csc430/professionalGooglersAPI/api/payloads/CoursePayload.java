package edu.cuny.csi.csc430.professionalGooglersAPI.api.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Date;

public class CoursePayload {
	private String name;
	private Integer teacher;
	private Integer maxCapacity;
	private Date startDate;
	private Date endDate;

	@JsonCreator
	public CoursePayload(String name, Integer teacher, Integer maxCapacity, Date startDate, Date endDate) {
		this.name = name;
		this.teacher = teacher;
		this.maxCapacity = maxCapacity;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getTeacher() {
		return teacher;
	}

	public Integer getMaxCapacity() {
		return maxCapacity;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}
}
