package net.schnellp.mycapnutrition.data;

import net.schnellp.mycapnutrition.data.DBContract.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class FoodDataSource {

    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] foodColNames;

    public FoodDataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(FoodEntry.TABLE_NAME, null, null, null, null, null, null);
        foodColNames = cursor.getColumnNames();
        cursor.close();
    }

    public void close() {
        dbHelper.close();
    }

    public Food createFood(String name, int referenceServing_mg,
                           int kcal, int carb_mg, int fat_mg, int protein_mg) {
        ContentValues values = new ContentValues();
        values.put(FoodEntry.COLUMN_NAME_NAME, name);
        values.put(FoodEntry.COLUMN_NAME_REF_SERVING_MG, referenceServing_mg);
        values.put(FoodEntry.COLUMN_NAME_KCAL, kcal);
        values.put(FoodEntry.COLUMN_NAME_CARB_MG, carb_mg);
        values.put(FoodEntry.COLUMN_NAME_FAT_MG, fat_mg);
        values.put(FoodEntry.COLUMN_NAME_PROTEIN_MG, protein_mg);

        long insertID = database.insert(FoodEntry.TABLE_NAME, null, values);
        Cursor cursor = database.query(FoodEntry.TABLE_NAME, foodColNames,
                FoodEntry._ID + " = " + insertID,
                null, null, null, null);
        Food newFood = new Food(cursor);
        cursor.close();

        return newFood;
    }

    public void deleteFood(Food food) {
        database.delete(FoodEntry.TABLE_NAME,
                FoodEntry._ID + " = " + food.DBID,
                null);
    }

    public List<Food> getAllFoods() {
        List<Food> foods = new ArrayList<>();

        Cursor cursor = database.query(FoodEntry.TABLE_NAME,
                foodColNames, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Food food = new Food(cursor);
            foods.add(food);
            cursor.moveToNext();
        }

        cursor.close();
        return foods;
    }
}
