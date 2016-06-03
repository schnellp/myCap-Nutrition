package net.schnellp.mycapnutrition.Model;

import android.os.Environment;
import android.util.Log;

import net.schnellp.mycapnutrition.MyCapNutrition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ExportManager {

    public static boolean exportData() {

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

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private static File getDataStorageDir() {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "/");
        return file;
    }

    private static void readRaw(){
        final File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS)
                .getAbsolutePath(), "myData.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
