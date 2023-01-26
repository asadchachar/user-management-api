package crn.hjemmeoppgave.api.service;

import crn.hjemmeoppgave.api.dao.IUserRepository;
import crn.hjemmeoppgave.api.dao.IUserRoleRepository;
import crn.hjemmeoppgave.api.dao.entities.UserRole;
import crn.hjemmeoppgave.api.dao.entities.Users;
import crn.hjemmeoppgave.api.resource.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IUserRoleRepository userRoleRepository;

    public Iterable<Users> findAllUsers() {
        return userRepository.findAll();
    }

    public List<Iterable<Users>> getUsersWithRoleInUnit(Integer unitId, Timestamp timestamp) {
        ArrayList<UserRole> userRoles = new ArrayList<>();
        this.userRoleRepository.findAll().forEach(userRoles::add);

        List<Integer> userIds = userRoles.stream().filter(u -> u.getUnitId() == unitId).filter(u -> (u.getValidTo() != null && timestamp != null) ? u.getValidTo().after(timestamp) : true).map(UserRole::getUserId).distinct().collect(Collectors.toList());

        return Arrays.asList(this.userRepository.findAllById(userIds));
    }

    public Users createUser(Users dbUser) {
        return userRepository.save(dbUser);
    }

    public int updateUser(Integer version, String name, Integer id) {
        return this.userRepository.updateVersionAndNameById(version, name, id);
    }

    public Boolean deleteUser(UserModel userModel) {
        Optional<List<UserRole>> role = this.userRoleRepository.findByUserId(userModel.getId());
        if (role.isPresent() && role.get().size() != 0) {
            // throw an exception
            return false;
        } else {
            Users user = new Users();
            user.setId(userModel.getId());
            this.userRepository.delete(user);
            return true;
        }
    }

    public UserRole createuserRole(UserRole userRole) {
        if (!this.userRepository.findById(userRole.getUserId()).isPresent()) {
            return null;
        }

        if (userRole.getValidTo() != null && userRole.getValidTo().before(userRole.getValidFrom())) {
            return null;
        }

        List<UserRole> existingRole = this.userRoleRepository.findByUserIdAndUnitIdAndRoleId(userRole.getUserId(), userRole.getUnitId(), userRole.getRoleId());
        if (existingRole != null && existingRole.size() > 0) {
            return null;
        }

        return this.userRoleRepository.save(userRole);

    }


    /*
    Mapper methods
     */

}
