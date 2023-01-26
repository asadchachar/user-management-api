package crn.hjemmeoppgave.api.resource.model;

import java.util.ArrayList;
import java.util.List;

public class UnitUsersModel {
    private Integer unitId;
    private List<UserRolesModel> userRoles;

    public UnitUsersModel() {
    }

    public UnitUsersModel(Integer unitId, List<UserRolesModel> userRoles) {
        this.unitId = unitId;
        this.userRoles = userRoles;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public List<UserRolesModel> getUserRoles() {
        if(this.userRoles == null)
            this.userRoles = new ArrayList<>();
        return userRoles;
    }

    public void setUserRoles(List<UserRolesModel> userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public String toString() {
        return "UnitUsersModel{" +
                "unitId=" + unitId +
                ", userRoles=" + userRoles +
                '}';
    }
}