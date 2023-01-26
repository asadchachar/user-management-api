package crn.hjemmeoppgave.api.service;

import crn.hjemmeoppgave.api.dao.IUnitRepository;
import crn.hjemmeoppgave.api.dao.IUserRepository;
import crn.hjemmeoppgave.api.dao.IUserRoleRepository;
import crn.hjemmeoppgave.api.dao.entities.Unit;
import crn.hjemmeoppgave.api.dao.entities.UserRole;
import crn.hjemmeoppgave.api.dao.entities.Users;
import crn.hjemmeoppgave.api.resource.model.UnitUsersModel;
import crn.hjemmeoppgave.api.resource.model.UserModel;
import crn.hjemmeoppgave.api.resource.model.UserRoleModel;
import crn.hjemmeoppgave.api.resource.model.UserRolesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UnitService {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IUserRoleRepository userRoleRepository;
    @Autowired
    IUnitRepository unitRepository;

    public Iterable<Unit> getAllUnits() {
        return this.unitRepository.findAll();
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

    public Optional<Unit> getUnitById(Integer unitId) {
        return this.unitRepository.findById(unitId);
    }
}
