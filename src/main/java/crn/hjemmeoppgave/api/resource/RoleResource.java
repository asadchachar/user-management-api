package crn.hjemmeoppgave.api.resource;

import crn.hjemmeoppgave.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@RestController
@RequestMapping("/role")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class RoleResource {

    @Autowired
    RoleService roleService;

    @GET
    @RequestMapping("/all")
    public ResponseEntity<Object> getAllRoles(
            @RequestHeader Map<String, String> headers) {
        return ResponseEntity.ok(this.roleService.getAllRoles());
    }

    /*
      Mapping methods
    */
}
