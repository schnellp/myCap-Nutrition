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

        if (cursor.isNull(3)) {
            quantity = new IntOrNA(0, true);
        } else {
            quantity = new IntOrNA(cursor.getInt(3));
        }

        unitName = cursor.getString(4);

        if (cursor.isNull(5)) {
            amount_mg = new IntOrNA(0, true);
        } else {
            amount_mg = new IntOrNA(cursor.getInt(5));
        }
        if (cursor.isNull(6)) {
            kcal = new IntOrNA(0, true);
        } else {
            kcal = new IntOrNA(cursor.getInt(6));
        }
        if (cursor.isNull(7)) {
            carb_mg = new IntOrNA(0, true);
        } else {
            carb_mg = new IntOrNA(cursor.getInt(7));
        }
        if (cursor.isNull(8)) {
            fat_mg = new IntOrNA(0, true);
        } else {
            fat_mg = new IntOrNA(cursor.getInt(8));
        }
        if (cursor.isNull(9)) {
            protein_mg = new IntOrNA(0, true);
        } else {
            protein_mg = new IntOrNA(cursor.getInt(9));
        }
    }
}
