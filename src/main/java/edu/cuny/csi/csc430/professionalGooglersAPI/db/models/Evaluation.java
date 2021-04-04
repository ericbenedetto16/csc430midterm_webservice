package edu.cuny.csi.csc430.professionalGooglersAPI.db.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(indexes = @Index(name = "student_teacher_unique", columnList = "teacher_id, student_id", unique = true))
public class Evaluation {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@JoinColumn(nullable = false)
	@ManyToOne
	private Faculty teacher;
	
	@JoinColumn(nullable = false)
	@ManyToOne
	private Student student;
	
	@Column(nullable = false)
	private Integer rating;


	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Faculty getTeacher() {
		return teacher;
	}
	
	public void setTeacher(Faculty teacher) {
		this.teacher = teacher;
	}
	
	public Student getStudent() {
		return student;
	}
	
	public void setStudent(Student student	) {
		this.student = student;
	}
	
	public Integer getRating() {
		return rating;
	}
	
	public void setRating(Integer rating) {
		this.rating = rating;
	}
}
