package edu.cuny.csi.csc430.professionalGooglersAPI.db.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Temporal;
import javax.persistence.GenerationType;
import javax.persistence.TemporalType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Course {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@JoinColumn(nullable = false)
	@ManyToOne
	private Faculty teacher;

	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Faculty getTeacher() {
		return teacher;
	}
	
	public void setTeacher(Faculty teacher) {
		this.teacher = teacher;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
