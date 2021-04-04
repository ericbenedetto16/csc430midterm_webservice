package edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories;

import org.springframework.data.repository.CrudRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Student;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.User;

public interface StudentRepository extends CrudRepository<Student, Integer> {
	public Student findByUser(User user);
}
