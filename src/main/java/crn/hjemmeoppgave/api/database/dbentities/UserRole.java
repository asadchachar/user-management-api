package crn.hjemmeoppgave.api.database.dbentities;

import java.sql.Timestamp;

public class UserRole {
    private int id;
    private int version;
    private int userId;
    private int unitId;
    private int roleId;
    private Timestamp validFrom;
    private Timestamp validTo;

    public UserRole(int id, int version, int userId, int unitId, int roleId, Timestamp validFrom, Timestamp validTo) {
        this.id = id;
        this.version = version;
        this.userId = userId;
        this.unitId = unitId;
        this.roleId = roleId;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
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
