package edu.cuny.csi.csc430.professionalGooglersAPI.api.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;

public class EvaluationPayload {
	private Integer studentId, teacherId, rating;

	@JsonCreator
	public EvaluationPayload(Integer studentId, Integer teacherId, Integer rating) {
		this.studentId = studentId;
		this.teacherId = teacherId;
		this.rating = rating;
	}
	
	public Integer getStudentId() {
		return studentId;
	}
	
	public Integer getTeacherId() {
		return teacherId;
	}
	
	public Integer getRating() {
		return rating;
	}
}
