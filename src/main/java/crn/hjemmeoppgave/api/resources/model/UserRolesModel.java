package crn.hjemmeoppgave.api.resources.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRolesModel {
    private UserModel user;
    private List<UserRoleModel> roles;

    public UserRolesModel() {
    }

    public UserRolesModel(UserModel user, List<UserRoleModel> roles) {
        this.user = user;
        this.roles = roles;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public List<UserRoleModel> getRoles() {
        if(this.roles == null)
            this.roles = new ArrayList<>();
        return roles;
    }

    public void setRoles(List<UserRoleModel> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UsersRoleModel{" +
                "user=" + user +
                ", roles=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRolesModel that = (UserRolesModel) o;
        return Objects.equals(user, that.user) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, roles);
    }
}
