package net.schnellp.mycapnutrition.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.schnellp.mycapnutrition.MyCapNutrition;

import java.util.ArrayList;
import java.util.List;

public class FoodManager extends DataObjectManager<Food> {

    public FoodManager(SQLiteDatabase db, String tableName) {
        super(db, tableName);
    }

    @Override
    protected Food fromCursor(Cursor cursor) {
        int dbid = cursor.getInt(cursor.getColumnIndex(DBContract._ID));

        ContentValues values = contentValuesFromCursor(cursor);

        return new Food(dbid, values);
    }

    protected List<Food> getByConstraint(String constraint) {
        List<Food> foods = new ArrayList<>();

        Cursor cursor = db.query(DBContract.FoodEntry.TABLE_NAME,
                null, // all columns
                constraint,
                null, // selection args
                null, // group by
                null, // having
                DBContract.FoodEntry.COLUMN_NAME_LAST_USED + " DESC"); // order by

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Food food = fromCursor(cursor);
            foods.add(food);
            cursor.moveToNext();
        }

        cursor.close();
        return foods;
    }

    @Override
    public boolean setActive(int dbid, boolean active) {
        ContentValues values = new ContentValues();
        values.put(DBContract.ObjectEntry._ACTIVE, active);

        try {
            db.update(tableName, values, DBContract.ObjectEntry._ID + " = " + dbid, null);

            List<Unit> units = MyCapNutrition.dataManager.getUnitsForFood(get(dbid), true);
            for (Unit unit : units) {
                if (!MyCapNutrition.dataManager.restoreUnit(unit)) {
                    return false;
                }
            }

            return true;
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
            return false;
        }
    }
}
