package crn.hjemmeoppgave.api.resources;
import crn.hjemmeoppgave.api.dao.IUserRepository;
import crn.hjemmeoppgave.api.dao.entities.Users;
import crn.hjemmeoppgave.api.resources.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UserResource {

    @Autowired
    IUserRepository userRepository;
    @GET
    @RequestMapping("/all")
    public ResponseEntity<Object> listAllUsers(
            @RequestHeader Map<String, String> headers) {
        return ResponseEntity.ok(Arrays.asList(userRepository.findAll()));
    }

    @POST
    @RequestMapping("/create")
    public ResponseEntity<Object> createNewUser (
            @RequestHeader Map<String, String> headers,
            @RequestBody UserModel userModel
            )
    {
        userRepository.save(map(userModel));
        return ResponseEntity.ok(userModel);
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
        dbUser.setId(userModel.getId());
        dbUser.setVersion(userModel.getVersion());
        dbUser.setName(userModel.getName());
        return dbUser;
//        return new Users(userModel.getId(), userModel.getVersion(), userModel.getName());
    }
}
