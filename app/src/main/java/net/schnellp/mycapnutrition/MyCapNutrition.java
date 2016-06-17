package net.schnellp.mycapnutrition;

import android.app.Application;
import android.util.Log;

import net.schnellp.mycapnutrition.model.DataManager;
import net.schnellp.mycapnutrition.model.TransportManager;

import java.io.IOException;

public class MyCapNutrition extends Application {

    public static DataManager dataManager;
    public static TransportManager transportManager;
    public static boolean databaseCreatedNow = false;

    @Override
    public void onCreate()
    {
        transportManager = new TransportManager(this);
        dataManager = new DataManager(this);

        if (databaseCreatedNow) {
            try {
                MyCapNutrition.transportManager.importData(
                        this.getApplicationContext().getAssets().open("usda-common-foods.mccsv"),
                        dataManager);
            } catch (IOException e) {
                Log.e("Exception","IOException"+String.valueOf(e.getMessage()));
                e.printStackTrace();
            }
            databaseCreatedNow = false;
        }

        super.onCreate();
    }
}
