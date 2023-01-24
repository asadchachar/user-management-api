package crn.hjemmeoppgave.api.resources;
import crn.hjemmeoppgave.api.dao.IUserRepository;
import crn.hjemmeoppgave.api.dao.IUserRoleRepository;
import crn.hjemmeoppgave.api.dao.entities.UserRole;
import crn.hjemmeoppgave.api.dao.entities.Users;
import crn.hjemmeoppgave.api.resources.model.UnitUsersModel;
import crn.hjemmeoppgave.api.resources.model.UserModel;
import crn.hjemmeoppgave.api.resources.model.UserRoleModel;
import crn.hjemmeoppgave.api.resources.model.UserRolesModel;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UserResource {

    @Autowired
    IUserRepository userRepository;
    @Autowired
    IUserRoleRepository userRoleRepository;
    @GET
    @RequestMapping("/all")
    public ResponseEntity<Object> listAllUsers(
            @RequestHeader Map<String, String> headers) {
        return ResponseEntity.ok(Arrays.asList(userRepository.findAll()));
    }

    @GET
    @RequestMapping("/validusers")
    public ResponseEntity<Object> listValidUsers(
            @RequestHeader Map<String, String> headers,
            @QueryParam("unitId") Integer unitId,
            @QueryParam("timestamp") Timestamp timestamp
            ) {
        ArrayList<UserRole> userRoles = new ArrayList<>();
        this.userRoleRepository.findAll().forEach(userRoles::add);

        List<Integer> userIds = userRoles
                .stream()
                .filter(u -> u.getUnitId() == unitId)
                .filter(u -> (u.getValidTo() != null && timestamp != null) ? u.getValidTo().after(timestamp) : true)
                .map(UserRole::getUserId)
                .distinct()
                .collect(Collectors.toList());

        return ResponseEntity.ok(Arrays.asList(this.userRepository.findAllById(userIds)));
    }

    @GET
    @RequestMapping("/validusersroles")
    public ResponseEntity<Object> listValidUserRoles(
            @RequestHeader Map<String, String> headers,
            @QueryParam("userId") Integer userId,
            @QueryParam("unitId") Integer unitId,
            @QueryParam("timestamp") Timestamp timestamp
    ) {
        ArrayList<UserRole> userRoles = new ArrayList<>();
        this.userRoleRepository.findAll().forEach(userRoles::add);

        List<UserRole> userRole = userRoles
                .stream()
                .filter(u -> u.getUserId() == userId)
                .filter(u -> u.getUnitId() == unitId)
                .filter(u -> (u.getValidTo() != null && timestamp != null) ? u.getValidTo().before(timestamp) : true)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Arrays.asList(userRole));
    }

    @POST
    @RequestMapping("/create")
    public ResponseEntity<Object> createNewUser (
            @RequestHeader Map<String, String> headers,
            @RequestBody UserModel userModel
            ) {
        Users usr = map(userModel);
        userRepository.save(usr);
        return ResponseEntity.ok(usr);
    }

    @PUT
    @RequestMapping("/update")
    public ResponseEntity<Object> updateUser(
            @RequestHeader Map<String, String> headers,
            @RequestBody UserModel userModel
    ) {
        this.userRepository.updateVersionAndNameById(userModel.getVersion(), userModel.getName(), userModel.getId());
        return ResponseEntity.ok(userModel);
    }

    @DELETE
    @RequestMapping("/delete")
    public ResponseEntity<Object> deleteUser(
            @RequestHeader Map<String, String> headers,
            @RequestBody UserModel userModel
    ) {
        Optional<List<UserRole>> role = this.userRoleRepository.findByUserId(userModel.getId());
        if (role.isPresent() && role.get().size() != 0) {
            // throw an exception
            return ResponseEntity.ok(false);
        }
        else {
            Users user = new Users();
            user.setId(userModel.getId());
            this.userRepository.delete(user);
            return ResponseEntity.ok(true);
        }

    }
    @POST
    @RequestMapping("/createUserRole")
    public ResponseEntity<Object> createUserRole (
            @RequestHeader Map<String, String> headers,
            @RequestBody UserRoleModel userRoleModel
    ) {
        UserRole userRole = mapRole(userRoleModel);

        if(!this.userRepository.findById(userRole.getUserId()).isPresent()) {
            return ResponseEntity.ok(false);
        }

        if(userRole.getValidTo() != null && userRole.getValidTo().before(userRole.getValidFrom())) {
            return ResponseEntity.ok(false);
        }

        List<UserRole> existingRole =
                this.userRoleRepository.findByUserIdAndUnitIdAndRoleId(userRole.getUserId(), userRole.getUnitId(), userRole.getRoleId());
        if(existingRole != null && existingRole.size() > 0) {
            return ResponseEntity.ok(false);
        }

        this.userRoleRepository.save(userRole);
        return ResponseEntity.ok(userRole);
    }
    @POST
    @RequestMapping("/unitUsersInfo")
    public ResponseEntity<Object> getUnitUsersInfo (
            @RequestHeader Map<String, String> headers,
            @QueryParam("unitId") Integer unitId
    ) {
        UnitUsersModel u = new UnitUsersModel();
        u.setUnitId(unitId);

        List<UserRole> allUnitRoles = this.userRoleRepository.findByUnitId(unitId);
        List<Integer> uniqueUserIds = allUnitRoles.stream().map(UserRole::getUserId).distinct().collect(Collectors.toList());

        for (Integer userId :
                uniqueUserIds) {
            UserRolesModel userRolesModel = new UserRolesModel();

            Users user = this.userRepository.findById(userId).get();
            userRolesModel.setUser(mapToUserModel(user));

            List<UserRole> userRoles = allUnitRoles.stream().filter(ur -> ur.getUserId() == userId).collect(Collectors.toList());
            userRolesModel.setRoles(userRoles.stream().map(this::mapToRoleModel).collect(Collectors.toList()));

            u.getUserRoles().add(userRolesModel);

        }

        return ResponseEntity.ok(u);
    }

    /*
      Mapping methods
    */

    private UserModel mapToUserModel(Users user) {
        UserModel um = new UserModel();
        um.setId(user.getId());
        um.setVersion(user.getVersion());
        um.setName(user.getName());
        return um;
    }

    private Users map(UserModel userModel) {
        Users dbUser = new Users();

        dbUser.setId(userModel.getId() != null ? userModel.getId() : getNextUserId());
        dbUser.setVersion( (userModel.getId() != null && userModel.getVersion() !=null) ? userModel.getVersion() : 1);
        dbUser.setName(userModel.getName());
        return dbUser;
//        return new Users(userModel.getId(), userModel.getVersion(), userModel.getName());
    }
    private UserRole mapRole(UserRoleModel userRoleModel) {
        UserRole userRole = new UserRole();

        userRole.setId(userRoleModel.getId() != null ? userRoleModel.getId() : getNextUserRoleId());
        userRole.setVersion(userRoleModel.getId() != null ? userRoleModel.getVersion() : 1);
        userRole.setUserId(userRoleModel.getUserId());
        userRole.setUnitId(userRoleModel.getUnitId());
        userRole.setRoleId(userRoleModel.getRoleId());
        userRole.setValidFrom(userRoleModel.getValidFrom() != null ? userRoleModel.getValidFrom() : Timestamp.valueOf(LocalDateTime.now()));
        userRole.setValidTo(userRoleModel.getValidTo());

        return userRole;
    }

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

    private Integer getNextUserRoleId() {
        ArrayList<UserRole> userRoles = new ArrayList<>();
        this.userRoleRepository.findAll().forEach(userRoles::add);

        int max = userRoles
                .stream()
                .mapToInt(UserRole::getId)
                .max()
                .getAsInt();
        return max + 1;
    }


    private Integer getNextUserId() {
        ArrayList<Users> users = new ArrayList<>();
        this.userRepository.findAll().forEach(users::add);
        int max = users
            .stream()
            .mapToInt(Users::getId)
            .max().getAsInt()
         ;
        return max + 1;
    }
}
