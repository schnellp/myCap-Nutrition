package net.schnellp.mycapnutrition.Model;

import net.schnellp.mycapnutrition.Model.DBContract.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Food.db";

    private static final String SQL_CREATE_TABLE_FOOD =
            "CREATE TABLE " + FoodEntry.TABLE_NAME + " (" +
                    FoodEntry._ID + " INTEGER PRIMARY KEY, " +
                    FoodEntry._ACTIVE + " INTEGER DEFAULT 1, " +
                    FoodEntry.COLUMN_NAME_NAME + " TEXT, " +
                    FoodEntry.COLUMN_NAME_REF_SERVING_MG + " INTEGER, " +
                    FoodEntry.COLUMN_NAME_KCAL + " INTEGER, " +
                    FoodEntry.COLUMN_NAME_CARB_MG + " INTEGER, " +
                    FoodEntry.COLUMN_NAME_FAT_MG + " INTEGER, " +
                    FoodEntry.COLUMN_NAME_PROTEIN_MG + " INTEGER)";

    private static final String SQL_CREATE_TABLE_UNIT =
            "CREATE TABLE " + UnitEntry.TABLE_NAME + " (" +
                    UnitEntry._ID + " INTEGER PRIMARY KEY, " +
                    UnitEntry._ACTIVE + " INTEGER DEFAULT 1, " +
                    UnitEntry.COLUMN_NAME_FOOD_ID + " INTEGER, " +
                    UnitEntry.COLUMN_NAME_NAME + " TEXT, " +
                    UnitEntry.COLUMN_NAME_AMOUNT_MG + " INTEGER, " +
                    "FOREIGN KEY (" + UnitEntry.COLUMN_NAME_FOOD_ID + ") " +
                    "REFERENCES " + FoodEntry.TABLE_NAME + " (" + FoodEntry._ID + ") " +
                    "ON DELETE RESTRICT)";

    private static final String SQL_CREATE_TABLE_RECORD =
            "CREATE TABLE " + RecordEntry.TABLE_NAME + " (" +
                    RecordEntry._ID + " INTEGER PRIMARY KEY, " +
                    RecordEntry._ACTIVE + " INTEGER DEFAULT 1, " +
                    RecordEntry.COLUMN_NAME_DATE + " TEXT, " +
                    RecordEntry.COLUMN_NAME_FOOD_ID + " INTEGER, " +
                    RecordEntry.COLUMN_NAME_UNIT_ID + " INTEGER, " +
                    RecordEntry.COLUMN_NAME_QUANTITY_CENTS + " INTEGER, " +
                    "FOREIGN KEY (" + RecordEntry.COLUMN_NAME_FOOD_ID + ") " +
                    "REFERENCES " + FoodEntry.TABLE_NAME + " (" + FoodEntry._ID + ") " +
                    "ON DELETE RESTRICT, " +
                    "FOREIGN KEY (" + RecordEntry.COLUMN_NAME_UNIT_ID + ") " +
                    "REFERENCES " + UnitEntry.TABLE_NAME + " (" + UnitEntry._ID + ") " +
                    "ON DELETE RESTRICT)";

    private static final String SQL_DELETE_FOOD =
            "DROP TABLE IF EXISTS " + FoodEntry.TABLE_NAME;
    private static final String SQL_DELETE_RECORD =
            "DROP TABLE IF EXISTS " + RecordEntry.TABLE_NAME;
    private static final String SQL_DELETE_UNIT =
            "DROP TABLE IF EXISTS " + UnitEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FOOD);
        db.execSQL(SQL_CREATE_TABLE_UNIT);
        db.execSQL(SQL_CREATE_TABLE_RECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_FOOD);
        db.execSQL(SQL_DELETE_RECORD);
        db.execSQL(SQL_DELETE_UNIT);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
