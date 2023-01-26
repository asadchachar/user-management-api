package crn.hjemmeoppgave.api.dao;

import crn.hjemmeoppgave.api.dao.entities.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IRoleRepository extends CrudRepository<Role, Integer> {

    @Override
    Optional<Role> findById(Integer integer);
}
