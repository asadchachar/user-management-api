package crn.hjemmeoppgave.api.resource;

import crn.hjemmeoppgave.api.service.UnitService;
import crn.hjemmeoppgave.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@RestController
@RequestMapping("/unit")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UnitResource {

    @Autowired
    UnitService unitService;

    @GET
    @RequestMapping("/all")
    public ResponseEntity<Object> listAllUnits(
            @RequestHeader Map<String, String> headers) {
        return ResponseEntity.ok(this.unitService.getAllUnits());
    }

    @GET
    @RequestMapping("/unitUsersInfo")
    public ResponseEntity<Object> getUnitUsersInfo(
            @RequestHeader Map<String, String> headers,
            @QueryParam("unitId") Integer unitId
    ) {
        return ResponseEntity.ok(this.unitService.getUnitUserInfo(unitId));
    }

    /*
      Mapping methods
    */
}
