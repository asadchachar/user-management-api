package crn.hjemmeoppgave.api.resources;
import crn.hjemmeoppgave.api.dao.IUserRepository;
import crn.hjemmeoppgave.api.dao.IUserRoleRepository;
import crn.hjemmeoppgave.api.dao.entities.UserRole;
import crn.hjemmeoppgave.api.dao.entities.Users;
import crn.hjemmeoppgave.api.resources.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Array;
import java.sql.Timestamp;
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
            )
    {
        Users usr = map(userModel);
        userRepository.save(usr);
        return ResponseEntity.ok(usr);
    }

    @PUT
    @RequestMapping("/update")
    public ResponseEntity<Object> updateUser(
            @RequestHeader Map<String, String> headers,
            @RequestBody UserModel userModel
    )
    {
        this.userRepository.updateVersionAndNameById(userModel.getVersion(), userModel.getName(), userModel.getId());
        return ResponseEntity.ok(userModel);
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
//        return new Users(userModel.getId(), userModel.getVersion(), userModel.getName());
    }

    private Integer getNextUserId() {
        ArrayList<Users> users = new ArrayList<>();
        this.userRepository.findAll().forEach(users::add);
        int a = users
            .stream()
            .mapToInt(Users::getId)
            .max().getAsInt()
         ;
        return a+1;
    }
}
