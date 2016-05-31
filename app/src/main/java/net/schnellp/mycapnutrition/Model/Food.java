package net.schnellp.mycapnutrition.Model;

import android.database.Cursor;

public class Food {

    public final int DBID;
    public final String name;
    public final IntOrNA referenceServing_mg;
    public final IntOrNA kcal, carb_mg, fat_mg, protein_mg;

    public Food(Cursor cursor) {
        DBID = cursor.getInt(0);
        name = cursor.getString(1);
        if (cursor.isNull(2)) {
            referenceServing_mg = new IntOrNA(0, true);
        } else {
            referenceServing_mg = new IntOrNA(cursor.getInt(2));
        }
        if (cursor.isNull(3)) {
            kcal = new IntOrNA(0, true);
        } else {
            kcal = new IntOrNA(cursor.getInt(3));
        }
        if (cursor.isNull(4)) {
            carb_mg = new IntOrNA(0, true);
        } else {
            carb_mg = new IntOrNA(cursor.getInt(4));
        }
        if (cursor.isNull(5)) {
            fat_mg = new IntOrNA(0, true);
        } else {
            fat_mg = new IntOrNA(cursor.getInt(5));
        }
        if (cursor.isNull(6)) {
            protein_mg = new IntOrNA(0, true);
        } else {
            protein_mg = new IntOrNA(cursor.getInt(6));
        }





    }
}
