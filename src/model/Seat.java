package model;

import java.io.Serializable;

public class Seat implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean seat;

    public Seat() {}

    public Seat(boolean seat) {
        this.seat = seat;
    }

    public boolean isSeat() {
        return seat;
    }

    public void setSeat(boolean seat) {
        this.seat = seat;
    }

    @Override
    public String toString() {
        return seat ? "[X]" : "[ ]";
    }
}
