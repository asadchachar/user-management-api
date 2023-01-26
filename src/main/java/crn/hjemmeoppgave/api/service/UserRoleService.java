package crn.hjemmeoppgave.api.service;

import crn.hjemmeoppgave.api.dao.IUserRepository;
import crn.hjemmeoppgave.api.dao.IUserRoleRepository;
import crn.hjemmeoppgave.api.dao.entities.UserRole;
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

    public Iterable<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll();
    }

    public List<List<UserRole>> getValidUserRoles(Integer userId, Integer unitId, Timestamp timestamp) {
        ArrayList<UserRole> userRoles = new ArrayList<>();
        this.userRoleRepository.findAll().forEach(userRoles::add);

        List<UserRole> userRole = userRoles.stream().filter(u -> u.getUserId() == userId).filter(u -> u.getUnitId() == unitId).filter(u -> (u.getValidTo() != null && timestamp != null) ? u.getValidTo().before(timestamp) : true).collect(Collectors.toList());

        return Arrays.asList(userRole);

    }

    public UserRole createuserRole(UserRole userRole) {
        if (!this.userRepository.findById(userRole.getUserId()).isPresent()) {
//            return ResponseEntity.ok(false);
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
