package net.schnellp.mycapnutrition.data;

public class Food {

    private String name = "null";
    private int referenceServing_mg = 1000;
    private int kcal, carb_mg, fat_mg, protein_mg = 0;

    public Food(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getKcal() {
        return kcal;
    }
}
