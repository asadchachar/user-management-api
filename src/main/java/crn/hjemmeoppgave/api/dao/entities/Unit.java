package crn.hjemmeoppgave.api.dao.entities;

import javax.persistence.*;

@Entity
@Table(name ="unit")
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "version")
    private Integer version;
    @Column(name = "name")
    private String name;

    public Unit(Integer id, Integer version, String name) {
        this.id = id;
        this.version = version;
        this.name = name;
    }

    public Unit() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setName(String name) {
        this.name = name;
    }
}
