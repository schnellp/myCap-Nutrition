package net.schnellp.mycapnutrition.Model;

import android.content.ContentValues;

public class DataObject {
    public final int DBID;

    public DataObject(int dbid) {
        this.DBID = dbid;
    }

    protected final ContentValues values = new ContentValues();
}
