package net.schnellp.mycapnutrition.Model;

public class Unit {

    public static final Unit ADD = new Unit("- New Unit -", 0);

    public final int DBID;
    public final int foodID;
    public final String name;
    public final IntOrNA amount_mg;

    public Unit(String name, int amount_mg) {
        DBID = -1;
        foodID = -1;
        this.name = name;
        this.amount_mg = new IntOrNA(amount_mg);
    }

    public Unit(int DBID, int foodID, String name, IntOrNA amount_mg) {
        this.DBID = DBID;
        this.foodID = foodID;
        this.name = name;
        this.amount_mg = amount_mg;
    }
}
