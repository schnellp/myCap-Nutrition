package net.schnellp.mycapnutrition.Model;

public class Ingredient {
    public final int DBID;
    public final int recipe_id;
    public final int food_id;
    public final int unit_id;
    public final IntOrNA quantity_cents;

    public final String foodName;
    public final String unitName;

    public Ingredient(int DBID, int recipe_id, int food_id, int unit_id, IntOrNA quantity_cents,
                      String foodName, String unitName) {
        this.DBID = DBID;
        this.recipe_id = recipe_id;
        this.food_id = food_id;
        this.unit_id = unit_id;
        this.quantity_cents = quantity_cents;
        this.foodName = foodName;
        this.unitName = unitName;
    }
}
