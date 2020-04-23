package MainPack.pack.Entity;

public class OrderCourse {
    private int orderID;
    private String nameCourse;
    private String ingredient1;
    private int weight1;
    private String ingredient2;
    private int weight2;
    private String ingredient3;
    private int weight3;

    public OrderCourse() {

    }

    public OrderCourse(int orderID, String nameCourse, String ingredient1, int weight1, String ingredient2, int weight2, String ingredient3, int weight3) {
        this.orderID = orderID;
        this.nameCourse = nameCourse;
        this.ingredient1 = ingredient1;
        this.weight1 = weight1;
        this.ingredient2 = ingredient2;
        this.weight2 = weight2;
        this.ingredient3 = ingredient3;
        this.weight3 = weight3;
    }

    public int getWeight1() {
        return weight1;
    }

    public void setWeight1(int weight1) {
        this.weight1 = weight1;
    }

    public int getWeight2() {
        return weight2;
    }

    public void setWeight2(int weight2) {
        this.weight2 = weight2;
    }

    public int getWeight3() {
        return weight3;
    }

    public void setWeight3(int weight3) {
        this.weight3 = weight3;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getNameCourse() {
        return nameCourse;
    }

    public void setNameCourse(String nameCourse) {
        this.nameCourse = nameCourse;
    }

    public String getIngredient1() {
        return ingredient1;
    }

    public void setIngredient1(String ingredient1) {
        this.ingredient1 = ingredient1;
    }

    public String getIngredient2() {
        return ingredient2;
    }

    public void setIngredient2(String ingredient2) {
        this.ingredient2 = ingredient2;
    }

    public String getIngredient3() {
        return ingredient3;
    }

    public void setIngredient3(String ingredient3) {
        this.ingredient3 = ingredient3;
    }

}
