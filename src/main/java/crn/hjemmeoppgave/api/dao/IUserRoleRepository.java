package crn.hjemmeoppgave.api.dao;

import crn.hjemmeoppgave.api.dao.entities.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IUserRoleRepository extends CrudRepository<UserRole, Integer> {
    List<UserRole> findByUserIdAndUnitIdAndRoleId(int userId, int unitId, int roleId);
    @Query("select u from UserRole u where u.userId = ?1")
    Optional<List<UserRole>> findByUserId(int userId);

}
