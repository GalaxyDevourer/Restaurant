package MainPack.pack.Entity;

public class Ingredient {
    private String ingredientName;
    private int portion;
    private int price;

    public Ingredient() {

    }

    public Ingredient(String ingredientName, int portion, int price) {
        this.ingredientName = ingredientName;
        this.portion = portion;
        this.price = price;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public int getPortion() {
        return portion;
    }

    public void setPortion(int portion) {
        this.portion = portion;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
