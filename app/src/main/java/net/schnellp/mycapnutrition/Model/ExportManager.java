package net.schnellp.mycapnutrition.Model;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import net.schnellp.mycapnutrition.MyCapNutrition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ExportManager {

    private MyCapNutrition app;

    public ExportManager(MyCapNutrition app) {
        this.app = app;
    }

    public boolean exportData() {

        //TODO: check for & request external storage permission

        File dir = getDataStorageDir();
        File file = new File(dir, "myCap Nutrition data.txt");

        String[] tables = new String[] {
                DBContract.FoodEntry.TABLE_NAME,
                DBContract.UnitEntry.TABLE_NAME,
                DBContract.RecordEntry.TABLE_NAME
        };

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);

            for (String tableName : tables) {
                pw.println(tableName);
                pw.println(MyCapNutrition.dataManager.tableToString(tableName, true));
            }

            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("Exception", "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private File getDataStorageDir() {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "/");
        return file;
    }

    public void importData(Uri uri){
        System.out.println("Importing...");
        try {
            InputStream inputStream = app.getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            inputStream.close();

            System.out.println(stringBuilder.toString());
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
