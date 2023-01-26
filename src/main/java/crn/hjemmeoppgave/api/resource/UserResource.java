package crn.hjemmeoppgave.api.resource;

import crn.hjemmeoppgave.api.dao.entities.Users;
import crn.hjemmeoppgave.api.error.ResponseCode;
import crn.hjemmeoppgave.api.error.UserException;
import crn.hjemmeoppgave.api.resource.model.UserModel;
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

        return ResponseEntity.ok(this.userService.getUsersWithRoleInUnit(unitId, timestamp));
    }

    @POST
    @RequestMapping("/create")
    public ResponseEntity<Object> createNewUser(
            @RequestHeader Map<String, String> headers,
            @RequestBody UserModel userModel
    ) {
        if (userModel == null)
            throw new UserException(ResponseCode.MISSING_DATA);

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

    /*
      Mapping methods
    */

    private Users map(UserModel userModel) {
        Users dbUser = new Users();

        dbUser.setId(userModel.getId() != null ? userModel.getId() : getNextUserId());
        dbUser.setVersion((userModel.getId() != null && userModel.getVersion() != null) ? userModel.getVersion() : 1);
        dbUser.setName(userModel.getName());
        return dbUser;
    }

    private Integer getNextUserId() {
        ArrayList<Users> users = new ArrayList<>();
        this.userService.getAllUsers().forEach(users::add);
        int max = users
                .stream()
                .mapToInt(Users::getId)
                .max().getAsInt();
        return max + 1;
    }
}
