package il.sce.scecafe.entity;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;

@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(cascade = javax.persistence.CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "ordID")
    private List<Position>positions;

    public List<Position> getPositions() {
        return positions;
    }

    @Nullable
    private Long baristaID = null;
    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public Long getBaristaID() {
        return baristaID;
    }

    public void setBaristaID(Long baristaID) {
        this.baristaID = baristaID;
    }

    public Orders(Long id, Long userID, String status) {
        this.id = id;
        this.userID = userID;
        this.status = status;
    }
    public Orders(Long userID, String status) {
        this.userID = userID;
        this.status = status;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String Status) {
        status = Status;
    }

    private Long userID;
    private String status;

    public Orders() {
    }

}
