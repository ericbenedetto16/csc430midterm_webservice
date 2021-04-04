package edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories;

import org.springframework.data.repository.CrudRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {

	Role findByRoleName(String roleName);
}
