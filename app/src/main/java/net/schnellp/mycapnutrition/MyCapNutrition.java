package net.schnellp.mycapnutrition;

import android.app.Application;

import net.schnellp.mycapnutrition.Model.DataManager;
import net.schnellp.mycapnutrition.Model.TransportManager;

public class MyCapNutrition extends Application {

    public static DataManager dataManager;
    public static TransportManager transportManager;

    @Override
    public void onCreate()
    {
        dataManager = new DataManager(this);
        transportManager = new TransportManager(this);
        super.onCreate();
    }
}
