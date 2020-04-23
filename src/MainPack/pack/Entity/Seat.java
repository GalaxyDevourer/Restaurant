package MainPack.pack.Entity;

public class Seat {
    private int seat;
    private String username;

    public Seat() {

    }

    public Seat(int seat, String username) {
        this.seat = seat;
        this.username = username;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
