package MainPack.pack.Entity;

import java.sql.Date;

public class Order {
    private int orderID;
    private String clientName;
    private String chefName;
    private int price;
    private String status;
    private String rate;
    private Date time;

    public Order() {

    }

    public Order(String clientName, String chefName, int price, String status, String rate, Date time) {
        this.clientName = clientName;
        this.chefName = chefName;
        this.price = price;
        this.status = status;
        this.rate = rate;
        this.time = time;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getChefName() {
        return chefName;
    }

    public void setChefName(String chefName) {
        this.chefName = chefName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
