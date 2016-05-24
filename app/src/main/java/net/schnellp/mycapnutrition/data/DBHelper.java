package net.schnellp.mycapnutrition.data;

import net.schnellp.mycapnutrition.data.DBContract.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Food.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FoodEntry.TABLE_NAME + " (" +
            FoodEntry._ID + " INTEGER PRIMARY KEY, " +
            FoodEntry.COLUMN_NAME_NAME + " TEXT, " +
            FoodEntry.COLUMN_NAME_REF_SERVING_MG + " INTEGER, " +
            FoodEntry.COLUMN_NAME_KCAL + " INTEGER, " +
            FoodEntry.COLUMN_NAME_CARB_MG + " INTEGER, " +
            FoodEntry.COLUMN_NAME_FAT_MG + " INTEGER, " +
            FoodEntry.COLUMN_NAME_PROTEIN_MG + " INTEGER); " +
            "CREATE TABLE " + RecordEntry.TABLE_NAME + " (" +
            RecordEntry._ID + " INTEGER PRIMARY KEY, " +
            RecordEntry.COLUMN_NAME_DATE + " TEXT, " +
            RecordEntry.COLUMN_NAME_FOOD + " INTEGER, " +
            "FOREIGN KEY(" + RecordEntry.COLUMN_NAME_FOOD + ") " +
            "REFERENCES " + FoodEntry.TABLE_NAME + "(" + FoodEntry._ID + "), " +
            RecordEntry.COLUMN_NAME_AMOUNT_MG + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " +
            FoodEntry.TABLE_NAME + ", " +
            RecordEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
