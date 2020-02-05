package io.github.aresgtr.floor;

public class FloorHandler {

    public Floor firstFloor;
    public Floor secondFloor;
    public Floor thirdFloor;

    public FloorHandler() {
        this.firstFloor = new Floor(1);
        this.secondFloor = new Floor(2);
        this.thirdFloor = new Floor(3);
    }
}
