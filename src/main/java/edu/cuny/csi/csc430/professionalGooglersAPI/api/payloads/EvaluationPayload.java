package edu.cuny.csi.csc430.professionalGooglersAPI.api.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;

public class EvaluationPayload {
	private Integer teacher, rating;

	@JsonCreator
	public EvaluationPayload(Integer teacher, Integer rating) {
		this.teacher = teacher;
		this.rating = rating;
	}
	
	public Integer getTeacherId() {
		return teacher;
	}
	
	public Integer getRating() {
		return rating;
	}
}
