package net.schnellp.mycapnutrition.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public abstract class DataObjectManager<T extends DataObject> {
    protected final SparseArray<T> objects = new SparseArray<>();
    protected final SQLiteDatabase db;
    protected final String tableName;
    protected final Class<T> dataObjectClass;

    public DataObjectManager(SQLiteDatabase db, String tableName, Class<T> dataObjectClass) {
        this.db = db;
        this.tableName = tableName;
        this.dataObjectClass = dataObjectClass;
    }

    public T get(int dbid) {
        T object = objects.get(dbid);

        if (object == null) {
            return read(dbid);
        } else {
            return object;
        }
    }

    public List<T> getAll() {
        return readAll();
    }

    private T read(int dbid) {
        Cursor cursor = db.query(DBContract.FoodEntry.TABLE_NAME,
                null, // all columns
                DBContract.ObjectEntry._ID + " = " + dbid,
                null, null, null, null);
        cursor.moveToFirst();
        T newObject = fromCursor(cursor);
        cursor.close();

        objects.append(newObject.DBID, newObject);

        return newObject;
    }

    private List<T> readAll() {
        List<T> newObjects = new ArrayList<>();

        Cursor cursor = db.query(tableName,
                null, // all columns
                null, // selection
                null, // selection args
                null, // group by
                null, // having
                null); // order by

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            T object = fromCursor(cursor);
            newObjects.add(object);
            this.objects.append(object.DBID, object);
            cursor.moveToNext();
        }

        cursor.close();
        return newObjects;
    }

    protected ContentValues contentValuesFromCursor(Cursor cursor) {
        ContentValues values = new ContentValues();

        for (int j = 0; j < cursor.getColumnCount(); j++) {
            values.put(cursor.getColumnName(j), cursor.getString(j));
        }

        return values;
    }

    protected T fromCursor(Cursor cursor) {
        int dbid = cursor.getInt(cursor.getColumnIndex(DBContract._ID));

        ContentValues values = contentValuesFromCursor(cursor);

        T object;
        try {
            object = dataObjectClass.getConstructor(int.class, ContentValues.class)
                    .newInstance(dbid, values);
        } catch (Exception e) {
            Log.e("Exception","SQLException" + String.valueOf(e.getMessage()));
            e.printStackTrace();
            return null;
        }

        return object;
    }

    public boolean setActive(int dbid, boolean active) {
        ContentValues values = new ContentValues();
        values.put(DBContract.ObjectEntry._ACTIVE, active);

        try {
            db.update(tableName, values, DBContract.ObjectEntry._ID + " = " + dbid, null);
            return true;
        } catch(SQLException e) {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
            return false;
        }
    }
}
