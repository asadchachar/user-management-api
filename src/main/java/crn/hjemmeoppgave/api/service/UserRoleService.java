package crn.hjemmeoppgave.api.service;

import crn.hjemmeoppgave.api.dao.IRoleRepository;
import crn.hjemmeoppgave.api.dao.IUserRepository;
import crn.hjemmeoppgave.api.dao.IUserRoleRepository;
import crn.hjemmeoppgave.api.dao.entities.UserRole;
import crn.hjemmeoppgave.api.error.ResponseCode;
import crn.hjemmeoppgave.api.error.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRoleService {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IUserRoleRepository userRoleRepository;
    @Autowired
    IRoleRepository roleRepository;

    public UserRoleService(IUserRepository userRepository, IUserRoleRepository userRoleRepository, IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    public Iterable<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll();
    }

    public List<List<UserRole>> getValidUserRoles(Integer userId, Integer unitId, Timestamp timestamp) {
        ArrayList<UserRole> userRoles = new ArrayList<>();
        this.userRoleRepository.findAll().forEach(userRoles::add);

        List<UserRole> userRole = userRoles.stream().filter(u -> u.getUserId() == userId).filter(u -> u.getUnitId() == unitId).filter(u -> u.getValidTo() == null || timestamp == null || u.getValidTo().after(timestamp)).collect(Collectors.toList());

        return Arrays.asList(userRole);

    }

    public UserRole createUserRole(UserRole userRole) {

        if (!this.roleRepository.findById(userRole.getRoleId()).isPresent())
            throw new UserException(ResponseCode.ROLE_DOES_NOT_EXIST);

        if (!this.userRepository.findById(userRole.getUserId()).isPresent()) {
            throw new UserException(ResponseCode.USER_DOES_NOT_EXIST);
        }

        if (userRole.getValidTo() != null && userRole.getValidTo().before(userRole.getValidFrom())) {
            throw new UserException(ResponseCode.VALIDTO_IS_BEFORE_VALIDFROM);
        }

        List<UserRole> existingRole = this.userRoleRepository.findByUserIdAndUnitIdAndRoleId(userRole.getUserId(), userRole.getUnitId(), userRole.getRoleId());
        if (existingRole != null && existingRole.size() > 0) {
            throw new UserException(ResponseCode.USER_ROLE_ALREADY_EXISTS);
        }

        return this.userRoleRepository.save(userRole);

    }

    /*
    Mapper methods
     */

}
