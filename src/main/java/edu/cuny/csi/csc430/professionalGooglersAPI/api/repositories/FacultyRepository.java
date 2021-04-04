package edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories;

import org.springframework.data.repository.CrudRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Faculty;

public interface FacultyRepository extends CrudRepository<Faculty, Integer> {
	
	Faculty findByUserId(Integer user);
}
