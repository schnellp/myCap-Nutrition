package net.schnellp.mycapnutrition.model;

import android.content.ContentValues;

public class DataObject {
    public final int DBID;

    public DataObject(int dbid, ContentValues values) {
        this.DBID = dbid;
        this.values.putAll(values);
    }

    protected final ContentValues values = new ContentValues();
}
