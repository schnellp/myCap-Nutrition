package net.schnellp.mycapnutrition.Model;

import android.database.Cursor;

public class Food {

    public final int DBID;
    public final String name;
    public final IntOrNA referenceServing_mg;
    public final IntOrNA kcal, carb_mg, fat_mg, protein_mg;

    public Food(int DBID, String name, IntOrNA referenceServing_mg,
                IntOrNA kcal, IntOrNA carb_mg, IntOrNA fat_mg, IntOrNA protein_mg) {
        this.DBID = DBID;
        this.name = name;
        this.referenceServing_mg = referenceServing_mg;
        this.kcal = kcal;
        this.carb_mg = carb_mg;
        this.fat_mg = fat_mg;
        this.protein_mg = protein_mg;
    }
}
