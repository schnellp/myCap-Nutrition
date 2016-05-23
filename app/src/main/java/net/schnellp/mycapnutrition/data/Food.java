package net.schnellp.mycapnutrition.data;

public class Food {

    public final int DBID;
    private String name = "null";
    private int referenceServing_mg = 1000;
    private int kcal, carb_mg, fat_mg, protein_mg = 0;

    public Food(int DBID) {
        this.DBID = DBID;
    }

    public Food setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public int getKcal() {
        return kcal;
    }
}
