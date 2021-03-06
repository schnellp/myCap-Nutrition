package net.schnellp.mycapnutrition.model;

import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.model.DBContract.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;

public class DBHelper extends SQLiteOpenHelper {

    private final Context context;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Food.db";

    private static final String SQL_CREATE_TABLE_PACKAGE =
            "CREATE TABLE " + PackageEntry.TABLE_NAME + " (" +
                    PackageEntry._ID + " INTEGER PRIMARY KEY, " +
                    PackageEntry._ACTIVE + " INTEGER DEFAULT 1, " +
                    PackageEntry.COLUMN_NAME_NAME + " TEXT DEFAULT 'Untitled', " +
                    PackageEntry.COLUMN_NAME_DESCRIPTION + " TEXT)";

    private static final String SQL_CREATE_TABLE_FOOD =
            "CREATE TABLE " + FoodEntry.TABLE_NAME + " (" +
                    FoodEntry._ID + " INTEGER PRIMARY KEY, " +
                    FoodEntry._ACTIVE + " INTEGER DEFAULT 1, " +
                    FoodEntry.COLUMN_NAME_PACKAGE + " INTEGER DEFAULT 1, " +
                    FoodEntry.COLUMN_NAME_TYPE + " INTEGER DEFAULT " + FoodEntry.TYPE_FOOD + ", " +
                    FoodEntry.COLUMN_NAME_NAME + " TEXT DEFAULT 'Unnamed', " +
                    FoodEntry.COLUMN_NAME_NOTES + " TEXT, " +
                    FoodEntry.COLUMN_NAME_RECIPE_SERVINGS + " INTEGER DEFAULT 1, " +
                    FoodEntry.COLUMN_NAME_REF_SERVING_MG + " INTEGER, " +
                    FoodEntry.COLUMN_NAME_KCAL + " INTEGER, " +
                    FoodEntry.COLUMN_NAME_CARB_MG + " INTEGER, " +
                    FoodEntry.COLUMN_NAME_FAT_MG + " INTEGER, " +
                    FoodEntry.COLUMN_NAME_PROTEIN_MG + " INTEGER, " +
                    FoodEntry.COLUMN_NAME_LAST_USED + " INTEGER DEFAULT 0, " +
                    "FOREIGN KEY (" + FoodEntry.COLUMN_NAME_PACKAGE + ") " +
                    "REFERENCES " + PackageEntry.TABLE_NAME + " (" + PackageEntry._ID + ") " +
                    "ON DELETE SET DEFAULT)";

    private static final String SQL_CREATE_TABLE_FTS_FOOD =
            "CREATE VIRTUAL TABLE " + FTSFoodEntry.TABLE_NAME + " " +
                    "USING fts4 (content='" + FoodEntry.TABLE_NAME + "', " +
                    FoodEntry.COLUMN_NAME_NAME + ")";

    private static final String SQL_CREATE_TABLE_UNIT =
            "CREATE TABLE " + UnitEntry.TABLE_NAME + " (" +
                    UnitEntry._ID + " INTEGER PRIMARY KEY, " +
                    UnitEntry._ACTIVE + " INTEGER DEFAULT 1, " +
                    UnitEntry.COLUMN_NAME_FOOD_ID + " INTEGER, " +
                    UnitEntry.COLUMN_NAME_NAME + " TEXT DEFAULT 'Unnamed', " +
                    UnitEntry.COLUMN_NAME_AMOUNT_MG + " INTEGER, " +
                    "FOREIGN KEY (" + UnitEntry.COLUMN_NAME_FOOD_ID + ") " +
                    "REFERENCES " + FoodEntry.TABLE_NAME + " (" + FoodEntry._ID + ") " +
                    "ON DELETE RESTRICT)";

    private static final String SQL_CREATE_TABLE_INGREDIENT =
            "CREATE TABLE " + IngredientEntry.TABLE_NAME + " (" +
                    IngredientEntry._ID + " INTEGER PRIMARY KEY, " +
                    IngredientEntry._ACTIVE + " INTEGER DEFAULT 1, " +
                    IngredientEntry.COLUMN_NAME_RECIPE_ID + " INTEGER " +
                    "REFERENCES " + FoodEntry.TABLE_NAME + " (" + FoodEntry._ID + ") " +
                    "ON DELETE CASCADE, " +
                    IngredientEntry.COLUMN_NAME_FOOD_ID + " INTEGER " +
                    "REFERENCES " + FoodEntry.TABLE_NAME + " (" + FoodEntry._ID + ") " +
                    "ON DELETE RESTRICT, " +
                    IngredientEntry.COLUMN_NAME_UNIT_ID + " INTEGER " +
                    "REFERENCES " + UnitEntry.TABLE_NAME + " (" + UnitEntry._ID + ") " +
                    "ON DELETE RESTRICT, " +
                    IngredientEntry.COLUMN_NAME_QUANTITY_CENTS + " INTEGER)";

    private static final String SQL_CREATE_TABLE_RECORD =
            "CREATE TABLE " + RecordEntry.TABLE_NAME + " (" +
                    RecordEntry._ID + " INTEGER PRIMARY KEY, " +
                    RecordEntry._ACTIVE + " INTEGER DEFAULT 1, " +
                    RecordEntry.COLUMN_NAME_DATE + " TEXT, " +
                    RecordEntry.COLUMN_NAME_TIME + " TEXT, " +
                    RecordEntry.COLUMN_NAME_FOOD_ID + " INTEGER, " +
                    RecordEntry.COLUMN_NAME_UNIT_ID + " INTEGER, " +
                    RecordEntry.COLUMN_NAME_QUANTITY_CENTS + " INTEGER, " +
                    "FOREIGN KEY (" + RecordEntry.COLUMN_NAME_FOOD_ID + ") " +
                    "REFERENCES " + FoodEntry.TABLE_NAME + " (" + FoodEntry._ID + ") " +
                    "ON DELETE RESTRICT, " +
                    "FOREIGN KEY (" + RecordEntry.COLUMN_NAME_UNIT_ID + ") " +
                    "REFERENCES " + UnitEntry.TABLE_NAME + " (" + UnitEntry._ID + ") " +
                    "ON DELETE RESTRICT)";

    private static final String SQL_CREATE_TABLE_BODYMASS =
            "CREATE TABLE " + BodyMassEntry.TABLE_NAME + " (" +
                    BodyMassEntry._ID + " INTEGER PRIMARY KEY, " +
                    BodyMassEntry._ACTIVE + " INTEGER DEFAULT 1, " +
                    BodyMassEntry.COLUMN_NAME_DATE + " TEXT, " +
                    BodyMassEntry.COLUMN_NAME_TIME + " TEXT, " +
                    BodyMassEntry.COLUMN_NAME_MASS_G + " INTEGER)";

    private static final String SQL_DELETE_PACKAGE =
            "DROP TABLE IF EXISTS " + PackageEntry.TABLE_NAME;
    private static final String SQL_DELETE_FOOD =
            "DROP TABLE IF EXISTS " + FoodEntry.TABLE_NAME;
    private static final String SQL_DELETE_FTS_FOOD =
            "DROP TABLE IF EXISTS " + FTSFoodEntry.TABLE_NAME;
    private static final String SQL_DELETE_UNIT =
            "DROP TABLE IF EXISTS " + UnitEntry.TABLE_NAME;
    private static final String SQL_DELETE_INGREDIENT =
            "DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME;
    private static final String SQL_DELETE_RECORD =
            "DROP TABLE IF EXISTS " + RecordEntry.TABLE_NAME;
    private static final String SQL_DELETE_BODYMASS =
            "DROP TABLE IF EXISTS " + BodyMassEntry.TABLE_NAME;

    private static final String SQL_TRIGGER_FTS_BEFORE_UPDATE =
            "CREATE TRIGGER Food_bu BEFORE UPDATE ON " + FoodEntry.TABLE_NAME + " BEGIN " +
                    "DELETE FROM " + FTSFoodEntry.TABLE_NAME + " " +
                    "WHERE " + "docid" + " = old." + "rowid" + "; " +
                    "END";

    private static final String SQL_TRIGGER_FTS_BEFORE_DELETE =
            "CREATE TRIGGER Food_bd BEFORE DELETE ON " + FoodEntry.TABLE_NAME + " BEGIN " +
                    "DELETE FROM " + FTSFoodEntry.TABLE_NAME + " " +
                    "WHERE " + "docid" + " = old." + "rowid" + "; " +
                    "END";

    private static final String SQL_TRIGGER_FTS_AFTER_UPDATE =
            "CREATE TRIGGER Food_au AFTER UPDATE ON " + FoodEntry.TABLE_NAME + " BEGIN " +
                    "INSERT INTO " + FTSFoodEntry.TABLE_NAME + " (" +
                    "docid" + ", " + FTSFoodEntry.COLUMN_NAME_NAME + ") " +
                    "VALUES (" +
                    "NEW.rowid" + ", NEW." + FoodEntry.COLUMN_NAME_NAME + "); " +
                    "END";

    private static final String SQL_TRIGGER_FTS_AFTER_INSERT =
            "CREATE TRIGGER Food_ai AFTER INSERT ON " + FoodEntry.TABLE_NAME + " BEGIN " +
                    "INSERT INTO " + FTSFoodEntry.TABLE_NAME + " (" +
                    "docid" + ", " + FTSFoodEntry.COLUMN_NAME_NAME + ") " +
                    "VALUES (" +
                    "NEW.rowid" + ", NEW." + FoodEntry.COLUMN_NAME_NAME + "); " +
                    "END";

    private static final String SQL_DROP_TRIGGER_FTS_BEFORE_DELETE =
            "DROP TRIGGER IF EXISTS Food_bd";
    private static final String SQL_DROP_TRIGGER_FTS_BEFORE_UPDATE =
            "DROP TRIGGER IF EXISTS Food_bu";
    private static final String SQL_DROP_TRIGGER_FTS_AFTER_UPDATE =
            "DROP TRIGGER IF EXISTS Food_au";
    private static final String SQL_DROP_TRIGGER_FTS_AFTER_INSERT =
            "DROP TRIGGER IF EXISTS Food_ai";

    private static final String SQL_INSERT_DEFAULT_PACKAGE =
            "INSERT INTO " + PackageEntry.TABLE_NAME + " (" +
                    PackageEntry.COLUMN_NAME_NAME + ", " +
                    PackageEntry.COLUMN_NAME_DESCRIPTION + ") " +
                    " VALUES ('My Package', 'Default package.')";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_PACKAGE);
        db.execSQL(SQL_INSERT_DEFAULT_PACKAGE);
        db.execSQL(SQL_CREATE_TABLE_FOOD);
        db.execSQL(SQL_CREATE_TABLE_FTS_FOOD);
        db.execSQL(SQL_CREATE_TABLE_UNIT);
        db.execSQL(SQL_CREATE_TABLE_INGREDIENT);
        db.execSQL(SQL_CREATE_TABLE_RECORD);
        db.execSQL(SQL_CREATE_TABLE_BODYMASS);

        db.execSQL(SQL_TRIGGER_FTS_BEFORE_UPDATE);
        db.execSQL(SQL_TRIGGER_FTS_BEFORE_DELETE);
        db.execSQL(SQL_TRIGGER_FTS_AFTER_UPDATE);
        db.execSQL(SQL_TRIGGER_FTS_AFTER_INSERT);

        MyCapNutrition.databaseCreatedNow = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetDatabase(db);
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

    public void resetDatabase(SQLiteDatabase db) {
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=OFF;");
        }
        db.execSQL(SQL_DROP_TRIGGER_FTS_BEFORE_UPDATE);
        db.execSQL(SQL_DROP_TRIGGER_FTS_BEFORE_DELETE);
        db.execSQL(SQL_DROP_TRIGGER_FTS_AFTER_UPDATE);
        db.execSQL(SQL_DROP_TRIGGER_FTS_AFTER_INSERT);

        db.execSQL(SQL_DELETE_BODYMASS);
        db.execSQL(SQL_DELETE_RECORD);
        db.execSQL(SQL_DELETE_INGREDIENT);
        db.execSQL(SQL_DELETE_UNIT);
        db.execSQL(SQL_DELETE_FTS_FOOD);
        db.execSQL(SQL_DELETE_FOOD);
        db.execSQL(SQL_DELETE_PACKAGE);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }

        onCreate(db);
    }
}
