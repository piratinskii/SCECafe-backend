package il.sce.scecafe.entity;

import jakarta.persistence.*;

@Entity
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long ordID;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @ManyToOne
    @JoinColumn(name = "itemID")
    private Item item;

    public Position(Long id, Long ordID, int count) {
        this.id = id;
        this.ordID = ordID;
        this.count = count;
    }

    public Position(Long ordID, int count) {
        this.ordID = ordID;
        this.count = count;
    }

    public Position(Long ordID, int count, Item item) {
        this.ordID = ordID;
        this.count = count;
        this.item = item;
    }

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Long getOrdID() {
        return ordID;
    }

    public void setOrdID(Long ordID) {
        this.ordID = ordID;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Position() {
    }

}
