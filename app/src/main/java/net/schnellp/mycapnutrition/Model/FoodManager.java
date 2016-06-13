package net.schnellp.mycapnutrition.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.schnellp.mycapnutrition.MyCapNutrition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FoodManager extends DataObjectManager<Food> {

    public FoodManager(SQLiteDatabase db) {
        super(db, DBContract.FoodEntry.TABLE_NAME, Food.class);
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
    public Food create(ContentValues values) {
        Food newFood = super.create(values);

        MyCapNutrition.dataManager.createUnit(newFood, "1 g", new IntOrNA(1000));

        return newFood;
    }

    public Food create(String name, IntOrNA referenceServing_mg,
                       IntOrNA kcal, IntOrNA carb_mg, IntOrNA fat_mg, IntOrNA protein_mg,
                       boolean active, int type) {
        ContentValues values = new ContentValues();
        values.put(DBContract.FoodEntry.COLUMN_NAME_NAME, name);
        if (!referenceServing_mg.isNA) {
            values.put(DBContract.FoodEntry.COLUMN_NAME_REF_SERVING_MG, referenceServing_mg.toString());
        }
        if (!kcal.isNA) {
            values.put(DBContract.FoodEntry.COLUMN_NAME_KCAL, kcal.toString());
        }
        if (!carb_mg.isNA) {
            values.put(DBContract.FoodEntry.COLUMN_NAME_CARB_MG, carb_mg.toString());
        }
        if (!fat_mg.isNA) {
            values.put(DBContract.FoodEntry.COLUMN_NAME_FAT_MG, fat_mg.toString());
        }
        if (!protein_mg.isNA) {
            values.put(DBContract.FoodEntry.COLUMN_NAME_PROTEIN_MG, protein_mg.toString());
        }

        values.put(DBContract.FoodEntry._ACTIVE, active);
        values.put(DBContract.FoodEntry.COLUMN_NAME_TYPE, type);

        return create(values);
    }

    public Food createFood(String name, IntOrNA referenceServing_mg,
                           IntOrNA kcal, IntOrNA carb_mg, IntOrNA fat_mg, IntOrNA protein_mg) {
        return create(name, referenceServing_mg,
                kcal, carb_mg, fat_mg, protein_mg, true, DBContract.FoodEntry.TYPE_FOOD);
    }

    public List<Food> getFoodMatches(String constraint, int foodType) {

        String sql = "SELECT * FROM " + DBContract.FoodEntry.TABLE_NAME + " " +
                "WHERE " + DBContract.FoodEntry._ID + " IN (" +
                "SELECT " + DBContract.FTSFoodEntry._DOCID + " " +
                "FROM " + DBContract.FTSFoodEntry.TABLE_NAME + " " +
                "WHERE " + DBContract.FTSFoodEntry.TABLE_NAME + " " +
                "MATCH ?) " +
                "AND " +
                DBContract.FoodEntry.COLUMN_NAME_TYPE + " = " + foodType;
        String[] args = { constraint };

        List<Food> newFoods = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, args);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Food food = fromCursor(cursor);
            newFoods.add(food);
            this.objects.append(food.DBID, food);
            cursor.moveToNext();
        }
        cursor.close();

        return newFoods;
    }

    public Food update(int dbid, String name, IntOrNA referenceServing_mg,
                           IntOrNA kcal, IntOrNA carb_mg, IntOrNA fat_mg, IntOrNA protein_mg) {
        ContentValues values = new ContentValues();
        values.put(DBContract.FoodEntry.COLUMN_NAME_NAME, name);
        if (!referenceServing_mg.isNA) {
            values.put(DBContract.FoodEntry.COLUMN_NAME_REF_SERVING_MG,
                    referenceServing_mg.toString());
        }
        if (!kcal.isNA) {
            values.put(DBContract.FoodEntry.COLUMN_NAME_KCAL, kcal.toString());
        }
        if (!carb_mg.isNA) {
            values.put(DBContract.FoodEntry.COLUMN_NAME_CARB_MG, carb_mg.toString());
        }
        if (!fat_mg.isNA) {
            values.put(DBContract.FoodEntry.COLUMN_NAME_FAT_MG, fat_mg.toString());
        }
        if (!protein_mg.isNA) {
            values.put(DBContract.FoodEntry.COLUMN_NAME_PROTEIN_MG, protein_mg.toString());
        }

        return update(dbid, values);
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

    public void touch(int dbid) {
        ContentValues values = new ContentValues();
        values.put(DBContract.FoodEntry.COLUMN_NAME_LAST_USED, (new Date()).getTime() / 1000);
        update(dbid, values);
    }

    public List<Food> getAllFoodsOfType(int type) {
        return getByConstraint(DBContract.FoodEntry._ACTIVE + " = 1" +
                " AND " + DBContract.FoodEntry.COLUMN_NAME_TYPE + " = " + type);
    }
}
