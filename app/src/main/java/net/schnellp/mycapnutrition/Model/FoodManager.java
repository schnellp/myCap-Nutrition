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

    protected Food fromCursor(Cursor cursor) {
        int DBID = cursor.getInt(cursor.getColumnIndex(DBContract._ID));
        String name = cursor.getString(cursor.getColumnIndex(DBContract.FoodEntry.COLUMN_NAME_NAME));
        IntOrNA referenceServing_mg;
        IntOrNA kcal;
        IntOrNA carb_mg;
        IntOrNA fat_mg;
        IntOrNA protein_mg;

        boolean isRecipe;
        IntOrNA servings;

        if (cursor.isNull(cursor.getColumnIndex(DBContract.FoodEntry.COLUMN_NAME_REF_SERVING_MG))) {
            referenceServing_mg = new IntOrNA(0, true);
        } else {
            referenceServing_mg = new IntOrNA(cursor.getInt(
                    cursor.getColumnIndex(DBContract.FoodEntry.COLUMN_NAME_REF_SERVING_MG)));
        }
        if (cursor.isNull(cursor.getColumnIndex(DBContract.FoodEntry.COLUMN_NAME_KCAL))) {
            kcal = new IntOrNA(0, true);
        } else {
            kcal = new IntOrNA(cursor.getInt(
                    cursor.getColumnIndex(DBContract.FoodEntry.COLUMN_NAME_KCAL)));
        }
        if (cursor.isNull(cursor.getColumnIndex(DBContract.FoodEntry.COLUMN_NAME_CARB_MG))) {
            carb_mg = new IntOrNA(0, true);
        } else {
            carb_mg = new IntOrNA(cursor.getInt(
                    cursor.getColumnIndex(DBContract.FoodEntry.COLUMN_NAME_CARB_MG)));
        }
        if (cursor.isNull(cursor.getColumnIndex(DBContract.FoodEntry.COLUMN_NAME_FAT_MG))) {
            fat_mg = new IntOrNA(0, true);
        } else {
            fat_mg = new IntOrNA(cursor.getInt(
                    cursor.getColumnIndex(DBContract.FoodEntry.COLUMN_NAME_FAT_MG)));
        }
        if (cursor.isNull(cursor.getColumnIndex(DBContract.FoodEntry.COLUMN_NAME_PROTEIN_MG))) {
            protein_mg = new IntOrNA(0, true);
        } else {
            protein_mg = new IntOrNA(cursor.getInt(
                    cursor.getColumnIndex(DBContract.FoodEntry.COLUMN_NAME_PROTEIN_MG)));
        }
        isRecipe = cursor.getInt(cursor.getColumnIndex(DBContract.FoodEntry.COLUMN_NAME_TYPE)) == 1;
        if (cursor.isNull(cursor.getColumnIndex(DBContract.FoodEntry.COLUMN_NAME_RECIPE_SERVINGS))) {
            servings = new IntOrNA(0, true);
        } else {
            servings = new IntOrNA(cursor.getInt(
                    cursor.getColumnIndex(DBContract.FoodEntry.COLUMN_NAME_RECIPE_SERVINGS)));
        }

        return new Food(DBID, name, referenceServing_mg, kcal, carb_mg, fat_mg, protein_mg,
                isRecipe, servings);
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
