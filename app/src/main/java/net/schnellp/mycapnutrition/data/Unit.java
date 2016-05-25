package net.schnellp.mycapnutrition.data;

import android.database.Cursor;

public class Unit {

    public static final Unit G = new Unit("g", 1000);
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

    public Unit(Cursor cursor) {
        DBID = cursor.getInt(0);
        foodID = cursor.getInt(1);
        name = cursor.getString(2);
        amount_mg = new IntOrNA(cursor.getInt(3));
    }
}
