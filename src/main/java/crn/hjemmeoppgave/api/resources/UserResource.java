package crn.hjemmeoppgave.api.resources;
import crn.hjemmeoppgave.api.dao.entities.UserRole;
import crn.hjemmeoppgave.api.dao.entities.Users;
import crn.hjemmeoppgave.api.resources.model.UserModel;
import crn.hjemmeoppgave.api.resources.model.UserRoleModel;
import crn.hjemmeoppgave.api.service.UserService;
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

@RestController
@RequestMapping("/user")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UserResource {

    @Autowired
    UserService userService;
    @GET
    @RequestMapping("/all")
    public ResponseEntity<Object> listAllUsers(
            @RequestHeader Map<String, String> headers) {
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GET
    @RequestMapping("/validusers")
    public ResponseEntity<Object> listValidUsers(
            @RequestHeader Map<String, String> headers,
            @QueryParam("unitId") Integer unitId,
            @QueryParam("timestamp") Timestamp timestamp
            ) {
        return ResponseEntity.ok(this.userService.getValidUsers(unitId, timestamp));
    }

    @GET
    @RequestMapping("/validusersroles")
    public ResponseEntity<Object> listValidUserRoles(
            @RequestHeader Map<String, String> headers,
            @QueryParam("userId") Integer userId,
            @QueryParam("unitId") Integer unitId,
            @QueryParam("timestamp") Timestamp timestamp
    ) {
        return ResponseEntity.ok(this.userService.getValidUserRoles(userId, unitId, timestamp));
    }

    @POST
    @RequestMapping("/create")
    public ResponseEntity<Object> createNewUser (
            @RequestHeader Map<String, String> headers,
            @RequestBody UserModel userModel
            ) {
        Users dbUser = map(userModel);

        return ResponseEntity.ok(this.userService.createUser(dbUser));
    }

    @PUT
    @RequestMapping("/update")
    public ResponseEntity<Object> updateUser(
            @RequestHeader Map<String, String> headers,
            @RequestBody UserModel userModel
    ) {
        this.userService.updateUser(userModel.getVersion(), userModel.getName(), userModel.getId());
        return ResponseEntity.ok(userModel);
    }

    @DELETE
    @RequestMapping("/delete")
    public ResponseEntity<Object> deleteUser(
            @RequestHeader Map<String, String> headers,
            @RequestBody UserModel userModel
    ) {
        return ResponseEntity.ok(this.userService.deleteUser(userModel));
    }
    @POST
    @RequestMapping("/createUserRole")
    public ResponseEntity<Object> createUserRole (
            @RequestHeader Map<String, String> headers,
            @RequestBody UserRoleModel userRoleModel
    ) {
        UserRole userRole = mapRole(userRoleModel);

        return ResponseEntity.ok(this.userService.createuserRole(userRole));
    }
    @GET
    @RequestMapping("/unitUsersInfo")
    public ResponseEntity<Object> getUnitUsersInfo (
            @RequestHeader Map<String, String> headers,
            @QueryParam("unitId") Integer unitId
    ) {
        return ResponseEntity.ok(this.userService.getUnitUserInfo(unitId));
    }

    /*
      Mapping methods
    */

    private Users map(UserModel userModel) {
        Users dbUser = new Users();

        dbUser.setId(userModel.getId() != null ? userModel.getId() : getNextUserId());
        dbUser.setVersion( (userModel.getId() != null && userModel.getVersion() !=null) ? userModel.getVersion() : 1);
        dbUser.setName(userModel.getName());
        return dbUser;
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

    private Integer getNextUserRoleId() {
        ArrayList<UserRole> userRoles = new ArrayList<>();
        this.userService.getAllUsersRoles().forEach(userRoles::add);

        int max = userRoles
                .stream()
                .mapToInt(UserRole::getId)
                .max()
                .getAsInt();
        return max + 1;
    }

    private Integer getNextUserId() {
        ArrayList<Users> users = new ArrayList<>();
        this.userService.findAllUsers().forEach(users::add);
        int max = users
            .stream()
            .mapToInt(Users::getId)
            .max().getAsInt();
        return max + 1;
    }
}
