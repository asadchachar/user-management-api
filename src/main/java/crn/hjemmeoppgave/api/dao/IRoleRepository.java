package crn.hjemmeoppgave.api.dao;

import crn.hjemmeoppgave.api.dao.entities.Role;
import org.springframework.data.repository.CrudRepository;

public interface IRoleRepository extends CrudRepository<Role, Integer> {

}
