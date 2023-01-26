package crn.hjemmeoppgave.api.resource.model;

import java.sql.Timestamp;

public class UserRoleModel {
    private Integer id;
    private Integer version;
    private Integer userId;
    private Integer unitId;
    private Integer roleId;
    private Timestamp validFrom;
    private Timestamp validTo;

    public UserRoleModel() {
    }

    public UserRoleModel(Integer id, Integer version, Integer userId, Integer unitId, Integer roleId, Timestamp validFrom, Timestamp validTo) {
        this.id = id;
        this.version = version;
        this.userId = userId;
        this.unitId = unitId;
        this.roleId = roleId;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Timestamp getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Timestamp validFrom) {
        this.validFrom = validFrom;
    }

    public Timestamp getValidTo() {
        return validTo;
    }

    public void setValidTo(Timestamp validTo) {
        this.validTo = validTo;
    }
}
