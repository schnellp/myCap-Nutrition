package net.schnellp.mycapnutrition.Model;

import net.schnellp.mycapnutrition.Model.DBContract.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
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

    public String tableToString(String tableName, boolean headers) {
        Cursor cursor = database.query(tableName, null, null, null, null, null, null);

        int ncol = cursor.getColumnCount();
        cursor.moveToFirst();

        StringBuilder tableString = new StringBuilder();

        if (headers) {
            String[] colnames = cursor.getColumnNames();
            for (int j = 0; j < ncol; j++) {
                tableString.append(colnames[j]);

                if (j < ncol - 1) {
                    tableString.append(",");
                } else {
                    tableString.append("\n");
                }
            }
        }

        while (!cursor.isAfterLast()) {
            for(int j = 0; j < ncol; j++) {
                if (cursor.isNull(j)) {

                } else if (cursor.getType(j) == Cursor.FIELD_TYPE_STRING) {
                    tableString.append("\"");
                    tableString.append(cursor.getString(j).replace("\"", "\"\""));
                    tableString.append("\"");
                } else {
                    tableString.append(cursor.getString(j));
                }

                if (j < ncol - 1) {
                    tableString.append(",");
                } else {
                    tableString.append("\n");
                }
            }
            cursor.moveToNext();
        }

        cursor.close();

        return tableString.toString();
    }

    public Food foodFromCursor(Cursor cursor) {
        int DBID = cursor.getInt(0);
        String name = cursor.getString(2);
        IntOrNA referenceServing_mg;
        IntOrNA kcal;
        IntOrNA carb_mg;
        IntOrNA fat_mg;
        IntOrNA protein_mg;

        if (cursor.isNull(3)) {
            referenceServing_mg = new IntOrNA(0, true);
        } else {
            referenceServing_mg = new IntOrNA(cursor.getInt(3));
        }
        if (cursor.isNull(4)) {
            kcal = new IntOrNA(0, true);
        } else {
            kcal = new IntOrNA(cursor.getInt(4));
        }
        if (cursor.isNull(5)) {
            carb_mg = new IntOrNA(0, true);
        } else {
            carb_mg = new IntOrNA(cursor.getInt(5));
        }
        if (cursor.isNull(6)) {
            fat_mg = new IntOrNA(0, true);
        } else {
            fat_mg = new IntOrNA(cursor.getInt(6));
        }
        if (cursor.isNull(7)) {
            protein_mg = new IntOrNA(0, true);
        } else {
            protein_mg = new IntOrNA(cursor.getInt(7));
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
        ContentValues values = new ContentValues();
        values.put(FoodEntry._ACTIVE, 1);

        try {
            database.update(FoodEntry.TABLE_NAME, values, FoodEntry._ID + " = " + food.DBID, null);
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }

        List<Unit> units = getUnitsForFood(food, true);
        for (Unit unit : units) {
            restoreUnit(unit);
        }

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

    public void touchFood(int dbid) {
        ContentValues values = new ContentValues();
        values.put(FoodEntry.COLUMN_NAME_LAST_USED, (new Date()).getTime() / 1000);
        try {
            database.update(FoodEntry.TABLE_NAME, values, FoodEntry._ID + " = " + dbid, null);
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }
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
                foodColNames,
                FoodEntry._ACTIVE + " = 1",
                null, // selection args
                null, // group by
                null, // having
                FoodEntry.COLUMN_NAME_LAST_USED + " DESC"); // order by

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Food food = foodFromCursor(cursor);
            foods.add(food);
            cursor.moveToNext();
        }

        cursor.close();
        return foods;
    }

    public void deactivateFood(Food food) {
        ContentValues values = new ContentValues();
        values.put(FoodEntry._ACTIVE, 0);

        try {
            database.update(FoodEntry.TABLE_NAME, values, FoodEntry._ID + " = " + food.DBID, null);
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }

        List<Unit> units = getUnitsForFood(food, true);
        for (Unit unit : units) {
            deactivateUnit(unit);
        }
    }

    private void deleteFood(Food food) {
        database.delete(FoodEntry.TABLE_NAME,
                FoodEntry._ID + " = " + food.DBID,
                null);
    }

    public Record recordFromCursor(Cursor cursor) {
        int DBID = cursor.getInt(0);
        String date = cursor.getString(2);

        int foodID = cursor.getInt(3);
        Food food = getFood(foodID);

        int unitID;
        if (cursor.isNull(4)) {
            unitID = -1;
        } else {
            unitID = cursor.getInt(4);
        }
        Unit unit = getUnit(unitID);

        IntOrNA quantity_cents;
        if (cursor.isNull(5)) {
            quantity_cents = new IntOrNA(0, true);
        } else {
            quantity_cents = new IntOrNA(cursor.getInt(5));
        }

        String foodName = food.name;
        String unitName = unit.name;

        IntOrNA amount_mg = unit.amount_mg.toDoubleOrNA().multiply(quantity_cents.toDoubleOrNA().divide(100)).round();
        DoubleOrNA servings = amount_mg.toDoubleOrNA().divide(food.referenceServing_mg.toDoubleOrNA());
        DoubleOrNA dKcal = servings.multiply(food.kcal.toDoubleOrNA());
        DoubleOrNA dCarb_mg = servings.multiply(food.carb_mg.toDoubleOrNA());
        DoubleOrNA dFat_mg = servings.multiply(food.fat_mg.toDoubleOrNA());
        DoubleOrNA dProtein_mg = servings.multiply(food.protein_mg.toDoubleOrNA());

        return new Record(DBID, date, foodID, unitID, foodName, unitName, quantity_cents,
                amount_mg, dKcal.round(), dCarb_mg.round(), dFat_mg.round(), dProtein_mg.round());
    }

    public Record createRecord(String date, Food food, IntOrNA quantity_cents, Unit unit) {
        touchFood(food.DBID);

        ContentValues values = new ContentValues();
        values.put(RecordEntry.COLUMN_NAME_DATE, date);
        values.put(RecordEntry.COLUMN_NAME_FOOD_ID, food.DBID);
        if (!quantity_cents.isNA) { values.put(RecordEntry.COLUMN_NAME_QUANTITY_CENTS, quantity_cents.toString()); }
        if (unit.DBID != -1) {
            values.put(RecordEntry.COLUMN_NAME_UNIT_ID, unit.DBID);
        }

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

    public Record updateRecord(int dbid, Food food, IntOrNA quantity_cents, Unit unit) {

        ContentValues values = new ContentValues();
        values.put(RecordEntry.COLUMN_NAME_FOOD_ID, food.DBID);
        if (!quantity_cents.isNA) { values.put(RecordEntry.COLUMN_NAME_QUANTITY_CENTS, quantity_cents.toString()); }
        if (unit.DBID != -1) {
            values.put(RecordEntry.COLUMN_NAME_UNIT_ID, unit.DBID);
        }

        long insertID = -1;
        try {
            database.update(RecordEntry.TABLE_NAME, values, RecordEntry._ID + " = " + dbid, null);
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }

        return getRecord(dbid);
    }

    public boolean restoreRecord(Record record) {
        ContentValues values = new ContentValues();
        values.put(RecordEntry._ACTIVE, 1);

        try {
            database.update(RecordEntry.TABLE_NAME, values,
                    RecordEntry._ID + " = " + record.DBID, null);
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
                recordColNames,
                RecordEntry._ACTIVE + " = 1",
                null, null, null, null);

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
                RecordEntry.COLUMN_NAME_DATE + " = ? AND " +
                RecordEntry._ACTIVE + " = 1",
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

    public void deactivateRecord(Record record) {
        ContentValues values = new ContentValues();
        values.put(RecordEntry._ACTIVE, 0);

        try {
            database.update(RecordEntry.TABLE_NAME, values,
                    RecordEntry._ID + " = " + record.DBID, null);
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }
    }

    private void deleteRecord(Record record) {
        database.delete(RecordEntry.TABLE_NAME,
                RecordEntry._ID + " = " + record.DBID,
                null);
    }

    public Unit unitFromCursor(Cursor cursor) {
        int DBID = cursor.getInt(0);
        int foodID = cursor.getInt(2);
        String name = cursor.getString(3);
        IntOrNA amount_mg;
        if (cursor.isNull(4)) {
            amount_mg = new IntOrNA(0, true);
        } else {
            amount_mg = new IntOrNA(cursor.getInt(4));
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

    public Unit updateUnit(int dbid, Food food, String name, IntOrNA amount_mg) {
        ContentValues values = new ContentValues();
        values.put(UnitEntry.COLUMN_NAME_FOOD_ID, food.DBID);
        values.put(UnitEntry.COLUMN_NAME_NAME, name);
        if (!amount_mg.isNA) { values.put(UnitEntry.COLUMN_NAME_AMOUNT_MG, amount_mg.val); }

        try {
            database.update(UnitEntry.TABLE_NAME, values, UnitEntry._ID + " = " + dbid, null);
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }

        return getUnit(dbid);
    }

    public Unit getUnit(int dbid) {
        if (dbid == -1) {
            return Unit.G;
        }

        Cursor cursor = database.query(UnitEntry.TABLE_NAME, unitColNames,
                UnitEntry._ID + " = " + dbid,
                null, null, null, null);
        cursor.moveToFirst();
        Unit newUnit = unitFromCursor(cursor);
        cursor.close();

        return newUnit;
    }

    public List<Unit> getUnitsForFood(Food food) {
        return getUnitsForFood(food, false);
    }

    public List<Unit> getUnitsForFood(Food food, boolean includeHidden) {
        List<Unit> units = new ArrayList<>();

        Cursor cursor;
        if (includeHidden) {
            cursor = database.query(UnitEntry.TABLE_NAME,
                    unitColNames,
                    UnitEntry.COLUMN_NAME_FOOD_ID + " = ?",
                    new String[] {"" + food.DBID}, null, null, null);
        } else {
            cursor = database.query(UnitEntry.TABLE_NAME,
                    unitColNames,
                    UnitEntry.COLUMN_NAME_FOOD_ID + " = ? AND " +
                    UnitEntry._ACTIVE + " = 1",
                    new String[] {"" + food.DBID}, null, null, null);
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            units.add(unitFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return units;
    }

    public void deactivateUnit(Unit unit) {
        ContentValues values = new ContentValues();
        values.put(UnitEntry._ACTIVE, 0);

        try {
            database.update(UnitEntry.TABLE_NAME, values,
                    UnitEntry._ID + " = " + unit.DBID, null);
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }
    }

    private void deleteUnit(Unit unit) {
        database.delete(UnitEntry.TABLE_NAME,
                UnitEntry._ID + " = " + unit.DBID,
                null);
    }

    public boolean restoreUnit(Unit unit) {
        ContentValues values = new ContentValues();
        values.put(UnitEntry._ACTIVE, 1);

        try {
            database.update(UnitEntry.TABLE_NAME, values,
                    UnitEntry._ID + " = " + unit.DBID, null);
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }

        return true;
    }

    public int directInsert(String tableName, ContentValues values) {
        long insertID = -1;
        try {
            insertID = database.insertOrThrow(tableName, null, values);
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }
        return (int) insertID;
    }

    public void clearData() {
        database.delete(RecordEntry.TABLE_NAME, null, null);
        database.delete(UnitEntry.TABLE_NAME, null, null);
        database.delete(FoodEntry.TABLE_NAME, null, null);
    }
}
