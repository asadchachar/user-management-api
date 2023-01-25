package crn.hjemmeoppgave.api.service;

import crn.hjemmeoppgave.api.dao.IUserRepository;
import crn.hjemmeoppgave.api.dao.IUserRoleRepository;
import crn.hjemmeoppgave.api.dao.entities.UserRole;
import crn.hjemmeoppgave.api.dao.entities.Users;
import crn.hjemmeoppgave.api.resources.model.UnitUsersModel;
import crn.hjemmeoppgave.api.resources.model.UserModel;
import crn.hjemmeoppgave.api.resources.model.UserRoleModel;
import crn.hjemmeoppgave.api.resources.model.UserRolesModel;
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
    public List<Iterable<Users>> getAllUsers() {
        return Arrays.asList(userRepository.findAll());
    }

    public Iterable<UserRole> getAllUsersRoles() {
        return userRoleRepository.findAll();
    }

    public Iterable<Users> findAllUsers() {
        return userRepository.findAll();
    }
    public List<Iterable<Users>> getValidUsers(Integer unitId, Timestamp timestamp) {
        ArrayList<UserRole> userRoles = new ArrayList<>();
        this.userRoleRepository.findAll().forEach(userRoles::add);

        List<Integer> userIds = userRoles
                .stream()
                .filter(u -> u.getUnitId() == unitId)
                .filter(u -> (u.getValidTo() != null && timestamp != null) ? u.getValidTo().after(timestamp) : true)
                .map(UserRole::getUserId)
                .distinct()
                .collect(Collectors.toList());

        return Arrays.asList(this.userRepository.findAllById(userIds));
    }

    public List<List<UserRole>> getValidUserRoles(Integer userId, Integer unitId, Timestamp timestamp) {
        ArrayList<UserRole> userRoles = new ArrayList<>();
        this.userRoleRepository.findAll().forEach(userRoles::add);

        List<UserRole> userRole = userRoles
                .stream()
                .filter(u -> u.getUserId() == userId)
                .filter(u -> u.getUnitId() == unitId)
                .filter(u -> (u.getValidTo() != null && timestamp != null) ? u.getValidTo().before(timestamp) : true)
                .collect(Collectors.toList());

        return Arrays.asList(userRole);

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
        }
        else {
            Users user = new Users();
            user.setId(userModel.getId());
            this.userRepository.delete(user);
            return true;
        }
    }

    public UserRole createuserRole(UserRole userRole) {
        if(!this.userRepository.findById(userRole.getUserId()).isPresent()) {
//            return ResponseEntity.ok(false);
            return null;
        }

        if(userRole.getValidTo() != null && userRole.getValidTo().before(userRole.getValidFrom())) {
            return null;
        }

        List<UserRole> existingRole =
                this.userRoleRepository.findByUserIdAndUnitIdAndRoleId(userRole.getUserId(), userRole.getUnitId(), userRole.getRoleId());
        if(existingRole != null && existingRole.size() > 0) {
            return null;
        }

        return this.userRoleRepository.save(userRole);

    }

    public UnitUsersModel getUnitUserInfo(Integer unitId) {
        UnitUsersModel unitWiseUsersInfo = new UnitUsersModel();
        unitWiseUsersInfo.setUnitId(unitId);

        List<UserRole> allUnitRoles = this.userRoleRepository.findByUnitId(unitId);
        List<Integer> uniqueUserIds = allUnitRoles.stream().map(UserRole::getUserId).distinct().collect(Collectors.toList());

        for (Integer userId : uniqueUserIds) {
            UserRolesModel userRolesModel = new UserRolesModel();

            Users user = this.userRepository.findById(userId).get();
            userRolesModel.setUser(mapToUserModel(user));

            List<UserRole> userRoles = allUnitRoles.stream().filter(ur -> ur.getUserId() == userId).collect(Collectors.toList());
            userRolesModel.setRoles(userRoles.stream().map(this::mapToRoleModel).collect(Collectors.toList()));

            unitWiseUsersInfo.getUserRoles().add(userRolesModel);
        }

        return unitWiseUsersInfo;
    }

    /*
    Mapper methods
     */

    private UserRoleModel mapToRoleModel(UserRole userRole) {
        UserRoleModel userRoleModel = new UserRoleModel();

        userRoleModel.setId(userRole.getId());
        userRoleModel.setVersion(userRole.getVersion());
        userRoleModel.setUserId(userRole.getUserId());
        userRoleModel.setUnitId(userRole.getUnitId());
        userRoleModel.setRoleId(userRole.getRoleId());
        userRoleModel.setValidFrom(userRole.getValidFrom());
        userRoleModel.setValidTo(userRole.getValidTo());

        return userRoleModel;
    }

    private UserModel mapToUserModel(Users user) {
        UserModel um = new UserModel();
        um.setId(user.getId());
        um.setVersion(user.getVersion());
        um.setName(user.getName());
        return um;
    }
}
