package net.schnellp.mycapnutrition.Model;

public class Record {
    public final int DBID;
    public final String date;
    public final String foodName, unitName;
    public final IntOrNA quantity, amount_mg;
    public final IntOrNA kcal, carb_mg, fat_mg, protein_mg;

    public Record(int DBID, String date, String foodName, String unitName,
                  IntOrNA quantity, IntOrNA amount_mg,
                  IntOrNA kcal, IntOrNA carb_mg, IntOrNA fat_mg, IntOrNA protein_mg) {
        this.DBID = DBID;
        this.date = date;
        this.foodName = foodName;
        this.unitName = unitName;
        this.quantity = quantity;
        this.amount_mg = amount_mg;
        this.kcal = kcal;
        this.carb_mg = carb_mg;
        this.fat_mg = fat_mg;
        this.protein_mg = protein_mg;
    }
}
