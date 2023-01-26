package crn.hjemmeoppgave.api.dao.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "userrole")
public class UserRole {
    @Id
    private Integer id;
    @Column(name = "version")
    private int version;
    @Column(name = "userId")
    private int userId;
    @Column(name = "unitId")
    private int unitId;
    @Column(name = "roleId")
    private int roleId;
    @Column(name = "validFrom")
    private Timestamp validFrom;
    @Column(name = "validTo")
    private Timestamp validTo;

    public UserRole() {
    }

    public UserRole(Integer id, int version, int userId, int unitId, int roleId, Timestamp validFrom, Timestamp validTo) {
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
