package net.schnellp.mycapnutrition.data;

import android.database.Cursor;

public class Record {
    public final int DBID;
    public final String date;
    public final String foodName, unitName;
    public final IntOrNA quantity, amount_mg;
    public final IntOrNA kcal, carb_mg, fat_mg, protein_mg;

    public Record(Cursor cursor) {
        DBID = cursor.getInt(0);
        date = cursor.getString(1);
        foodName = cursor.getString(2);
        quantity = new IntOrNA(cursor.getInt(3));
        unitName = cursor.getString(4);
        amount_mg = new IntOrNA(cursor.getInt(5));
        kcal = new IntOrNA(cursor.getInt(6));
        carb_mg = new IntOrNA(cursor.getInt(7));
        fat_mg = new IntOrNA(cursor.getInt(8));
        protein_mg = new IntOrNA(cursor.getInt(9));
    }
}
