package crn.hjemmeoppgave.api.dao;

import crn.hjemmeoppgave.api.dao.entities.Users;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface IUserRepository extends CrudRepository<Users, Integer> {
    @Transactional
    @Modifying
    @Query("update Users u set u.version = ?1, u.name = ?2 where u.id = ?3")
    int updateVersionAndNameById(Integer version, String name, Integer id);

}
