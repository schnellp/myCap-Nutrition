package net.schnellp.mycapnutrition.Model;

import net.schnellp.mycapnutrition.Model.DBContract.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private SQLiteDatabase database;
    private String[] foodColNames;
    private String[] recordColNames;
    private String[] unitColNames;

    public DataManager(Context context) {
        DBHelper dbHelper = new DBHelper(context);

        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.query(FoodEntry.TABLE_NAME, null, null, null, null, null, null);
        foodColNames = cursor.getColumnNames();

        cursor = database.query(RecordEntry.TABLE_NAME, null, null, null, null, null, null);
        recordColNames = cursor.getColumnNames();

        cursor = database.query(UnitEntry.TABLE_NAME, null, null, null, null, null, null);
        unitColNames = cursor.getColumnNames();

        cursor.close();
    }

    public Food foodFromCursor(Cursor cursor) {
        int DBID = cursor.getInt(0);
        String name = cursor.getString(1);
        IntOrNA referenceServing_mg;
        IntOrNA kcal;
        IntOrNA carb_mg;
        IntOrNA fat_mg;
        IntOrNA protein_mg;

        if (cursor.isNull(2)) {
            referenceServing_mg = new IntOrNA(0, true);
        } else {
            referenceServing_mg = new IntOrNA(cursor.getInt(2));
        }
        if (cursor.isNull(3)) {
            kcal = new IntOrNA(0, true);
        } else {
            kcal = new IntOrNA(cursor.getInt(3));
        }
        if (cursor.isNull(4)) {
            carb_mg = new IntOrNA(0, true);
        } else {
            carb_mg = new IntOrNA(cursor.getInt(4));
        }
        if (cursor.isNull(5)) {
            fat_mg = new IntOrNA(0, true);
        } else {
            fat_mg = new IntOrNA(cursor.getInt(5));
        }
        if (cursor.isNull(6)) {
            protein_mg = new IntOrNA(0, true);
        } else {
            protein_mg = new IntOrNA(cursor.getInt(6));
        }

        return new Food(DBID, name, referenceServing_mg, kcal, carb_mg, fat_mg, protein_mg);
    }

    public Food createFood(String name, IntOrNA referenceServing_mg,
                           IntOrNA kcal, IntOrNA carb_mg, IntOrNA fat_mg, IntOrNA protein_mg) {
        ContentValues values = new ContentValues();
        values.put(FoodEntry.COLUMN_NAME_NAME, name);
        if (!referenceServing_mg.isNA) {
            values.put(FoodEntry.COLUMN_NAME_REF_SERVING_MG, referenceServing_mg.toString());
        }
        if (!kcal.isNA) {
            values.put(FoodEntry.COLUMN_NAME_KCAL, kcal.toString());
        }
        if (!carb_mg.isNA) {
            values.put(FoodEntry.COLUMN_NAME_CARB_MG, carb_mg.toString());
        }
        if (!fat_mg.isNA) {
            values.put(FoodEntry.COLUMN_NAME_FAT_MG, fat_mg.toString());
        }
        if (!protein_mg.isNA) {
            values.put(FoodEntry.COLUMN_NAME_PROTEIN_MG, protein_mg.toString());
        }

        long insertID = -1;
        try {
            insertID = database.insertOrThrow(FoodEntry.TABLE_NAME, null, values);
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }

        return getFood((int) insertID);
    }

    public boolean restoreFood(Food food) {
        createFood(food.name, food.referenceServing_mg, food.kcal,
                food.carb_mg, food.fat_mg, food.protein_mg);

        return true;
    }

    public Food updateFood(int dbid, String name, IntOrNA referenceServing_mg,
                           IntOrNA kcal, IntOrNA carb_mg, IntOrNA fat_mg, IntOrNA protein_mg) {
        ContentValues values = new ContentValues();
        values.put(FoodEntry.COLUMN_NAME_NAME, name);
        if (!referenceServing_mg.isNA) {
            values.put(FoodEntry.COLUMN_NAME_REF_SERVING_MG, referenceServing_mg.toString());
        }
        if (!kcal.isNA) {
            values.put(FoodEntry.COLUMN_NAME_KCAL, kcal.toString());
        }
        if (!carb_mg.isNA) {
            values.put(FoodEntry.COLUMN_NAME_CARB_MG, carb_mg.toString());
        }
        if (!fat_mg.isNA) {
            values.put(FoodEntry.COLUMN_NAME_FAT_MG, fat_mg.toString());
        }
        if (!protein_mg.isNA) {
            values.put(FoodEntry.COLUMN_NAME_PROTEIN_MG, protein_mg.toString());
        }

        try {
            database.update(FoodEntry.TABLE_NAME, values, FoodEntry._ID + " = " + dbid, null);
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }

        return getFood(dbid);
    }

    public Food getFood(int dbid) {
        Cursor cursor = database.query(FoodEntry.TABLE_NAME, foodColNames,
                FoodEntry._ID + " = " + dbid,
                null, null, null, null);
        cursor.moveToFirst();
        Food newFood = foodFromCursor(cursor);
        cursor.close();

        return newFood;
    }

    public List<Food> getAllFoods() {
        List<Food> foods = new ArrayList<>();

        Cursor cursor = database.query(FoodEntry.TABLE_NAME,
                foodColNames, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Food food = foodFromCursor(cursor);
            foods.add(food);
            cursor.moveToNext();
        }

        cursor.close();
        return foods;
    }

    public void deleteFood(Food food) {
        database.delete(FoodEntry.TABLE_NAME,
                FoodEntry._ID + " = " + food.DBID,
                null);
    }

    public Record recordFromCursor(Cursor cursor) {
        int DBID = cursor.getInt(0);
        String date = cursor.getString(1);
        String foodName = cursor.getString(2);
        String unitName = cursor.getString(4);
        IntOrNA quantity;
        IntOrNA amount_mg;
        IntOrNA kcal;
        IntOrNA carb_mg;
        IntOrNA fat_mg;
        IntOrNA protein_mg;

        if (cursor.isNull(3)) {
            quantity = new IntOrNA(0, true);
        } else {
            quantity = new IntOrNA(cursor.getInt(3));
        }



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

        return new Record(DBID, date, foodName, unitName, quantity,
                amount_mg, kcal, carb_mg, fat_mg, protein_mg);
    }

    public Record createRecord(String date, Food food, IntOrNA quantity_cents, Unit unit) {
        DoubleOrNA dQuantity = quantity_cents.toDoubleOrNA().divide(100);
        DoubleOrNA dAmount_mg = unit.amount_mg.toDoubleOrNA().multiply(dQuantity);
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
        if (!quantity_cents.isNA) { values.put(RecordEntry.COLUMN_NAME_QUANTITY, quantity_cents.toString()); }
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

        System.out.println("SAVE " + insertID);

        return getRecord((int) insertID);
    }

    public boolean restoreRecord(Record record) {
        ContentValues values = new ContentValues();
        values.put(RecordEntry.COLUMN_NAME_DATE, record.date);
        values.put(RecordEntry.COLUMN_NAME_FOOD_NAME, record.foodName);
        if (!record.quantity_cents.isNA) { values.put(RecordEntry.COLUMN_NAME_QUANTITY, record.quantity_cents.val); }
        values.put(RecordEntry.COLUMN_NAME_UNIT, record.unitName);
        if (!record.amount_mg.isNA) { values.put(RecordEntry.COLUMN_NAME_AMOUNT_MG, record.amount_mg.val); }
        if (!record.kcal.isNA) { values.put(RecordEntry.COLUMN_NAME_KCAL, record.kcal.val); }
        if (!record.carb_mg.isNA) { values.put(RecordEntry.COLUMN_NAME_CARB_MG, record.carb_mg.val); }
        if (!record.fat_mg.isNA) { values.put(RecordEntry.COLUMN_NAME_FAT_MG, record.fat_mg.val); }
        if (!record.protein_mg.isNA) { values.put(RecordEntry.COLUMN_NAME_PROTEIN_MG, record.protein_mg.val); }

        try {
            database.insertOrThrow(RecordEntry.TABLE_NAME, null, values);
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }

        return true;
    }

    public Record getRecord(int dbid) {
        Cursor cursor = database.query(RecordEntry.TABLE_NAME, recordColNames,
                RecordEntry._ID + " = " + dbid,
                null, null, null, null);
        cursor.moveToFirst();
        Record newRecord = recordFromCursor(cursor);
        cursor.close();

        return newRecord;
    }

    public List<Record> getAllRecords() {
        List<Record> records = new ArrayList<>();

        Cursor cursor = database.query(RecordEntry.TABLE_NAME,
                recordColNames, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Record record = recordFromCursor(cursor);
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
            Record record = recordFromCursor(cursor);
            records.add(record);
            cursor.moveToNext();
        }

        cursor.close();

        return records;
    }

    public void deleteRecord(Record record) {
        database.delete(RecordEntry.TABLE_NAME,
                RecordEntry._ID + " = " + record.DBID,
                null);
    }

    public Unit unitFromCursor(Cursor cursor) {
        int DBID = cursor.getInt(0);
        int foodID = cursor.getInt(1);
        String name = cursor.getString(2);
        IntOrNA amount_mg;
        if (cursor.isNull(3)) {
            amount_mg = new IntOrNA(0, true);
        } else {
            amount_mg = new IntOrNA(cursor.getInt(3));
        }

        return new Unit(DBID, foodID, name, amount_mg);
    }

    public Unit createUnit(Food food, String name, IntOrNA amount_mg) {
        ContentValues values = new ContentValues();
        values.put(UnitEntry.COLUMN_NAME_FOOD_ID, food.DBID);
        values.put(UnitEntry.COLUMN_NAME_NAME, name);
        if (!amount_mg.isNA) { values.put(UnitEntry.COLUMN_NAME_AMOUNT_MG, amount_mg.val); }

        long insertID = -1;
        try {
            insertID = database.insertOrThrow(UnitEntry.TABLE_NAME, null, values);
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }

        return getUnit((int) insertID);
    }

    public Unit getUnit(int dbid) {
        Cursor cursor = database.query(UnitEntry.TABLE_NAME, unitColNames,
                UnitEntry._ID + " = " + dbid,
                null, null, null, null);
        cursor.moveToFirst();
        Unit newUnit = unitFromCursor(cursor);
        cursor.close();

        return newUnit;
    }

    public List<Unit> getUnitsForFood(Food food) {
        List<Unit> units = new ArrayList<>();

        Cursor cursor = database.query(UnitEntry.TABLE_NAME,
                unitColNames,
                UnitEntry.COLUMN_NAME_FOOD_ID + " = ?",
                new String[] {"" + food.DBID}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            units.add(unitFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return units;
    }

    public void deleteUnit(Unit unit) {
        database.delete(UnitEntry.TABLE_NAME,
                UnitEntry._ID + " = " + unit.DBID,
                null);
    }

    public boolean restoreUnit(Unit unit) {
        ContentValues values = new ContentValues();
        values.put(UnitEntry.COLUMN_NAME_NAME, unit.name);
        values.put(UnitEntry.COLUMN_NAME_FOOD_ID, unit.foodID);
        if (!unit.amount_mg.isNA) { values.put(UnitEntry.COLUMN_NAME_AMOUNT_MG, unit.amount_mg.val); }

        try {
            database.insertOrThrow(UnitEntry.TABLE_NAME, null, values);
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }

        return true;
    }
}
