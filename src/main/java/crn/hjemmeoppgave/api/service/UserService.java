package crn.hjemmeoppgave.api.service;

import crn.hjemmeoppgave.api.dao.IUserRepository;
import crn.hjemmeoppgave.api.dao.IUserRoleRepository;
import crn.hjemmeoppgave.api.dao.entities.UserRole;
import crn.hjemmeoppgave.api.dao.entities.Users;
import crn.hjemmeoppgave.api.error.ResponseCode;
import crn.hjemmeoppgave.api.error.UserException;
import crn.hjemmeoppgave.api.resource.model.ResponseModel;
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

    public Iterable<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Users> getUserById(Integer userId) {
        return this.userRepository.findById(userId);
    }
    public List<Iterable<Users>> getUsersWithRoleInUnit(Integer unitId, Timestamp timestamp) {
        if(unitId == null)
            throw new UserException(ResponseCode.UNIT_ID_REQUIRED);
        if(timestamp == null)
            throw new UserException(ResponseCode.TIMESTAMP_REQUIRED);

        ArrayList<UserRole> userRoles = new ArrayList<>();
        this.userRoleRepository.findAll().forEach(userRoles::add);

        List<Integer> userIds = userRoles.stream().filter(u -> u.getUnitId() == unitId).filter(u -> u.getValidTo() == null || timestamp == null || u.getValidTo().after(timestamp)).map(UserRole::getUserId).distinct().collect(Collectors.toList());

        return Arrays.asList(this.userRepository.findAllById(userIds));
    }

    public Users createUser(Users user) {
        if(user == null || user.getId() == null || user.getId() == 0)
            throw new UserException(ResponseCode.USER_ID_REQUIRED);
        return userRepository.save(user);
    }

    public int updateUser(Integer version, String name, Integer id) {
        if(id == null || id == 0)
            throw new UserException(ResponseCode.USER_ID_REQUIRED);

        return this.userRepository.updateVersionAndNameById(version, name, id);
    }

    public ResponseModel deleteUser(UserModel userModel) {
        if(userModel == null || userModel.getId() == null || userModel.getId() == 0)
            throw new UserException(ResponseCode.USER_ID_REQUIRED);

        Optional<List<UserRole>> role = this.userRoleRepository.findByUserId(userModel.getId());
        if (role.isPresent() && role.get().size() != 0) {
            throw new UserException(ResponseCode.ROLES_ALREADY_EXISTS);
        } else {
            Users user = new Users();
            user.setId(userModel.getId());
            this.userRepository.delete(user);
            return new ResponseModel("User Deleted");
        }
    }

    /*
    Mapper methods
     */

}
