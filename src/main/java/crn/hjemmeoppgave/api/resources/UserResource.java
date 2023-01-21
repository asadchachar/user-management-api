package crn.hjemmeoppgave.api.resources;
import crn.hjemmeoppgave.api.resources.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
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
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @GET
    @RequestMapping("/all")
    public ResponseEntity<Object> listAllUsers(
            @RequestHeader Map<String, String> headers)
            {
        return ResponseEntity.ok(Arrays.asList(new String [] {"user", "user2"}));
    }

    @POST
    @RequestMapping("/create")
    public ResponseEntity<Object> createNewUser(
            @RequestHeader Map<String, String> headers,
            @RequestBody UserModel userModel
            )
    {
        return ResponseEntity.ok(userModel);
    }

    @PUT
    @RequestMapping("/update")
    public ResponseEntity<Object> updateUser(
            @RequestHeader Map<String, String> headers,
            @RequestBody UserModel userModel
    )
    {
        return ResponseEntity.ok(userModel);
    }
}
