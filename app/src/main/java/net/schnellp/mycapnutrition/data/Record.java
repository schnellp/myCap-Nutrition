package net.schnellp.mycapnutrition.data;

import android.database.Cursor;

public class Record {
    public final int DBID;
    public final String date;
    public final String foodName;
    public final IntOrNA amount_mg;
    public final IntOrNA kcal, carb_mg, fat_mg, protein_mg;

    public Record(Cursor cursor) {
        DBID = cursor.getInt(0);
        date = cursor.getString(1);
        foodName = cursor.getString(2);
        amount_mg = new IntOrNA(cursor.getInt(3));
        kcal = new IntOrNA(cursor.getInt(4));
        carb_mg = new IntOrNA(cursor.getInt(5));
        fat_mg = new IntOrNA(cursor.getInt(6));
        protein_mg = new IntOrNA(cursor.getInt(7));
    }
}
