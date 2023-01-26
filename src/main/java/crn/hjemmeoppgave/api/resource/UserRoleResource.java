package crn.hjemmeoppgave.api.resource;

import crn.hjemmeoppgave.api.dao.entities.UserRole;
import crn.hjemmeoppgave.api.error.ResponseCode;
import crn.hjemmeoppgave.api.error.UserException;
import crn.hjemmeoppgave.api.resource.model.UserRoleModel;
import crn.hjemmeoppgave.api.service.RoleService;
import crn.hjemmeoppgave.api.service.UserRoleService;
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
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/userrole")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UserRoleResource {

    @Autowired
    UserRoleService userRoleService;

    @GET
    @RequestMapping("/all")
    public ResponseEntity<Object> listAllUserRoles(
            @RequestHeader Map<String, String> headers) {
        return ResponseEntity.ok(this.userRoleService.getAllUserRoles());
    }

    @GET
    @RequestMapping("/validusersroles")
    public ResponseEntity<Object> listValidUserRoles(
            @RequestHeader Map<String, String> headers,
            @QueryParam("userId") Integer userId,
            @QueryParam("unitId") Integer unitId,
            @QueryParam("timestamp") Timestamp timestamp
    ) {
        return ResponseEntity.ok(this.userRoleService.getValidUserRoles(userId, unitId, timestamp));
    }

    @POST
    @RequestMapping("/createUserRole")
    public ResponseEntity<Object> createUserRole(
            @RequestHeader Map<String, String> headers,
            @RequestBody UserRoleModel userRoleModel
    ) {
        if(userRoleModel == null)
            throw new UserException(ResponseCode.MISSING_DATA);
        if (userRoleModel.getRoleId() == null)
            throw new UserException(ResponseCode.ROLE_ID_REQUIRED);
        if (userRoleModel.getUserId() == null)
            throw new UserException(ResponseCode.USER_ID_REQUIRED);

        UserRole userRole = mapRole(userRoleModel);

        return ResponseEntity.ok(this.userRoleService.createuserRole(userRole));
    }

    /*
      Mapping methods
    */

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
        this.userRoleService.getAllUserRoles().forEach(userRoles::add);

        int max = userRoles
                .stream()
                .mapToInt(UserRole::getId)
                .max()
                .getAsInt();
        return max + 1;
    }
}
