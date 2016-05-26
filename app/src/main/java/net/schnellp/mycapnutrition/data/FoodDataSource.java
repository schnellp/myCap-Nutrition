package net.schnellp.mycapnutrition.data;

import net.schnellp.mycapnutrition.data.DBContract.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FoodDataSource {

    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] foodColNames;
    private String[] recordColNames;

    public FoodDataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.query(FoodEntry.TABLE_NAME, null, null, null, null, null, null);
        foodColNames = cursor.getColumnNames();

        cursor = database.query(RecordEntry.TABLE_NAME, null, null, null, null, null, null);
        recordColNames = cursor.getColumnNames();

        cursor.close();
    }

    public void close() {
        dbHelper.close();
    }

    private String[] getMutableFoodColNames() {
        return Arrays.copyOfRange(foodColNames, 1, foodColNames.length);
    }

    public Food createFood(String name, String referenceServing_mg,
                           String kcal, String carb_mg, String fat_mg, String protein_mg) {
        ContentValues values = new ContentValues();
        values.put(FoodEntry.COLUMN_NAME_NAME, name);
        values.put(FoodEntry.COLUMN_NAME_REF_SERVING_MG, referenceServing_mg);
        values.put(FoodEntry.COLUMN_NAME_KCAL, kcal);
        values.put(FoodEntry.COLUMN_NAME_CARB_MG, carb_mg);
        values.put(FoodEntry.COLUMN_NAME_FAT_MG, fat_mg);
        values.put(FoodEntry.COLUMN_NAME_PROTEIN_MG, protein_mg);

        long insertID = -1;
        try {
            insertID = database.insertOrThrow(FoodEntry.TABLE_NAME, null, values);
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }

        Cursor cursor = database.query(FoodEntry.TABLE_NAME, foodColNames,
                FoodEntry._ID + " = " + insertID,
                null, null, null, null);
        cursor.moveToFirst();
        Food newFood = new Food(cursor);
        cursor.close();

        return newFood;
    }

    public Food updateFood(int dbid, String name, String referenceServing_mg,
                           String kcal, String carb_mg, String fat_mg, String protein_mg) {
        ContentValues values = new ContentValues();
        values.put(FoodEntry.COLUMN_NAME_NAME, name);
        values.put(FoodEntry.COLUMN_NAME_REF_SERVING_MG, referenceServing_mg);
        values.put(FoodEntry.COLUMN_NAME_KCAL, kcal);
        values.put(FoodEntry.COLUMN_NAME_CARB_MG, carb_mg);
        values.put(FoodEntry.COLUMN_NAME_FAT_MG, fat_mg);
        values.put(FoodEntry.COLUMN_NAME_PROTEIN_MG, protein_mg);

        String[] mutableFoodColNames = getMutableFoodColNames();

        try {
            database.update(FoodEntry.TABLE_NAME, values, FoodEntry._ID + " = " + dbid, null);
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }

        Cursor cursor = database.query(FoodEntry.TABLE_NAME, foodColNames,
                FoodEntry._ID + " = " + dbid,
                null, null, null, null);
        cursor.moveToFirst();
        Food newFood = new Food(cursor);
        cursor.close();

        return newFood;
    }

    public Record createRecord(String date, Food food, IntOrNA amount, Unit unit) {
        DoubleOrNA dAmount_mg = unit.amount_mg.multiply(amount).toDoubleOrNA();
        DoubleOrNA dRefServ_mg = new DoubleOrNA(food.referenceServing_mg);
        DoubleOrNA dServ = dAmount_mg.divide(dRefServ_mg);

        DoubleOrNA dKcal = new DoubleOrNA(food.kcal).multiply(dServ);
        DoubleOrNA dCarb_mg = new DoubleOrNA(food.carb_mg).multiply(dServ);
        DoubleOrNA dFat_mg = new DoubleOrNA(food.fat_mg).multiply(dServ);
        DoubleOrNA dProtein_mg = new DoubleOrNA(food.protein_mg).multiply(dServ);

        String sKcal = dKcal.round().toString();
        String sCarb_mg = dCarb_mg.round().toString();
        String sFat_mg = dFat_mg.round().toString();
        String sProtein_mg = dProtein_mg.round().toString();

        ContentValues values = new ContentValues();
        values.put(RecordEntry.COLUMN_NAME_DATE, date);
        values.put(RecordEntry.COLUMN_NAME_FOOD_NAME, food.name);
        if (!amount.isNA) { values.put(RecordEntry.COLUMN_NAME_QUANTITY, amount.toString()); }
        values.put(RecordEntry.COLUMN_NAME_UNIT, unit.name);
        if (!dAmount_mg.isNA) { values.put(RecordEntry.COLUMN_NAME_AMOUNT_MG, dAmount_mg.round().toString()); }
        if (!dKcal.isNA) { values.put(RecordEntry.COLUMN_NAME_KCAL, sKcal); }
        if (!dCarb_mg.isNA) { values.put(RecordEntry.COLUMN_NAME_CARB_MG, sCarb_mg); }
        if (!dFat_mg.isNA) { values.put(RecordEntry.COLUMN_NAME_FAT_MG, sFat_mg); }
        if (!dProtein_mg.isNA) { values.put(RecordEntry.COLUMN_NAME_PROTEIN_MG, sProtein_mg); }

        long insertID = -1;
        try {
            insertID = database.insertOrThrow(RecordEntry.TABLE_NAME, null, values);
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }

        Cursor cursor = database.query(RecordEntry.TABLE_NAME, recordColNames,
                RecordEntry._ID + " = " + insertID,
                null, null, null, null);
        cursor.moveToFirst();
        Record newRecord = new Record(cursor);
        cursor.close();

        return newRecord;
    }

    public Food getFood(int dbid) {
        Cursor cursor = database.query(FoodEntry.TABLE_NAME, foodColNames,
                FoodEntry._ID + " = " + dbid,
                null, null, null, null);
        cursor.moveToFirst();
        Food newFood = new Food(cursor);
        cursor.close();

        return newFood;
    }

    public void deleteFood(Food food) {
        database.delete(FoodEntry.TABLE_NAME,
                FoodEntry._ID + " = " + food.DBID,
                null);
    }

    public void deleteRecord(Record record) {
        database.delete(RecordEntry.TABLE_NAME,
                RecordEntry._ID + " = " + record.DBID,
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

    public List<Record> getAllRecords() {
        List<Record> records = new ArrayList<>();

        Cursor cursor = database.query(RecordEntry.TABLE_NAME,
                recordColNames, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Record record = new Record(cursor);
            records.add(record);
            cursor.moveToNext();
        }

        cursor.close();
        return records;
    }

    public List<Record> getRecordsFromDate(String date) {
        List<Record> records = new ArrayList<>();

        Cursor cursor = database.query(RecordEntry.TABLE_NAME,
                recordColNames,
                RecordEntry.COLUMN_NAME_DATE + " = ?",
                new String[] {date}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Record record = new Record(cursor);
            records.add(record);
            cursor.moveToNext();
        }

        cursor.close();

        return records;
    }
}
