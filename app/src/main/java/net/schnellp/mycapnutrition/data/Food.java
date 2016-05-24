package net.schnellp.mycapnutrition.data;

import android.database.Cursor;

public class Food {

    public final int DBID;
    public final String name;
    public final int referenceServing_mg;
    public final int kcal, carb_mg, fat_mg, protein_mg;

    public Food(Cursor cursor) {
        DBID = cursor.getInt(0);
        name = cursor.getString(1);
        referenceServing_mg = cursor.getInt(2);
        kcal = cursor.getInt(3);
        carb_mg = cursor.getInt(4);
        fat_mg = cursor.getInt(5);
        protein_mg = cursor.getInt(6);
    }
}
