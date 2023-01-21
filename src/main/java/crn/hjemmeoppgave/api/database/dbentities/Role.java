package crn.hjemmeoppgave.api.database.dbentities;

public class Role {
    private int id;
    private int version;
    private String name;

    public Role(int id, int version, String name) {
        this.id = id;
        this.version = version;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setName(String name) {
        this.name = name;
    }
}
