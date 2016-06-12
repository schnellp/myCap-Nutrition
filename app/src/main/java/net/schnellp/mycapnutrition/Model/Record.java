package net.schnellp.mycapnutrition.model;

public class Record {
    public final int DBID;
    public final String date;
    public final int foodID, unitID;
    public final String foodName, unitName;
    public final IntOrNA quantity_cents, amount_mg;
    public final IntOrNA kcal, carb_mg, fat_mg, protein_mg;

    public Record(int DBID, String date, int foodID, int unitID,
                  String foodName, String unitName,
                  IntOrNA quantity_cents, IntOrNA amount_mg,
                  IntOrNA kcal, IntOrNA carb_mg, IntOrNA fat_mg, IntOrNA protein_mg) {
        this.DBID = DBID;
        this.date = date;
        this.foodID = foodID;
        this.unitID = unitID;
        this.foodName = foodName;
        this.unitName = unitName;
        this.quantity_cents = quantity_cents;
        this.amount_mg = amount_mg;
        this.kcal = kcal;
        this.carb_mg = carb_mg;
        this.fat_mg = fat_mg;
        this.protein_mg = protein_mg;
    }
}
