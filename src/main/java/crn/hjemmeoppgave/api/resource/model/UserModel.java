package crn.hjemmeoppgave.api.resource.model;

public class UserModel {
    private Integer id;
    private Integer version;
    private String name;

    public UserModel() {
    }

    public UserModel(Integer id, Integer version, String name) {
        this.id = id;
        this.version = version;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setName(String name) {
        this.name = name;
    }
}
