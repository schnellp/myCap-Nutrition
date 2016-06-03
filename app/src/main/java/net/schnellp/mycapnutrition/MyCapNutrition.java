package net.schnellp.mycapnutrition;

import android.app.Application;

import net.schnellp.mycapnutrition.Model.DataManager;
import net.schnellp.mycapnutrition.Model.ExportManager;

public class MyCapNutrition extends Application {

    public static DataManager dataManager;
    public static ExportManager exportManager;

    @Override
    public void onCreate()
    {
        dataManager = new DataManager(this);
        exportManager = new ExportManager(this);
        super.onCreate();
    }
}
