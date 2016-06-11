package net.schnellp.mycapnutrition.Model;

import android.database.Cursor;
import android.util.SparseArray;

public abstract class DataObjectManager<T extends DataObject> {
    protected final SparseArray<T> objects = new SparseArray<>();

    public T get(int dbid) {
        T object = objects.get(dbid);

        if (object == null) {
            return read(dbid);
        } else {
            return object;
        }
    }

    private T read(int dbid) {
        return null;
    }

    protected abstract T fromCursor(Cursor cursor);
}
