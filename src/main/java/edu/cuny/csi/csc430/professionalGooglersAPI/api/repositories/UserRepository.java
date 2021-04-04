package edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories;

import org.springframework.data.repository.CrudRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.User;

public interface UserRepository extends CrudRepository<User, Integer> {

	User findByFirstNameAndLastName(String firstName, String lastName);
	User findByEmail(String email);
}
