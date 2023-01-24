package crn.hjemmeoppgave.api.dao;

import crn.hjemmeoppgave.api.dao.entities.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface IUserRoleRepository extends CrudRepository<UserRole, Integer> {

}
