package net.schnellp.mycapnutrition;

import android.app.Application;

import net.schnellp.mycapnutrition.Model.DataManager;

public class MyCapNutrition extends Application {

    public static DataManager dataManager;

    @Override
    public void onCreate()
    {
        dataManager = new DataManager(this);
        super.onCreate();
    }
}
