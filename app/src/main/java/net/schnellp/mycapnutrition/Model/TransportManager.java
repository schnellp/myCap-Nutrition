package net.schnellp.mycapnutrition.Model;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;

import net.schnellp.mycapnutrition.MyCapNutrition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class TransportManager {

    private MyCapNutrition app;

    public TransportManager(MyCapNutrition app) {
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

    private boolean hasEnclosingDoubleQuotes(String string) {
        return string.charAt(0) == '"' &&
                string.charAt(string.length() - 1) == '"';
    }

    private String stripEnclosingDoubleQuotes(String string) {
        if (!string.equals("") &&
                string.charAt(0) == '"' &&
                string.charAt(string.length() - 1) == '"') {
            string = string.substring(1, string.length() - 1);
        }
        return string;
    }

    private ArrayList<String> getEntriesFromLine(String line) {
        ArrayList<String> entries = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder builder = new StringBuilder();
        char curChar;

        for (int i = 0; i < line.length(); i++) {
            curChar = line.charAt(i);
            switch (curChar) {
                case ',':
                    entries.add(builder.toString());
                    builder.delete(0, builder.length());
                    break;
                case '"':
                    inQuotes = !inQuotes;
                    // intentionally continue
                default:
                    builder.append(curChar);
            }
        }
        entries.add(builder.toString());

        return entries;
    }

    private ContentValues contentValuesFromLineEntries(ArrayList<String> colnames,
                                                       ArrayList<String> lineEntries) {
        ContentValues values = new ContentValues();
        for (int j = 0; j < colnames.size(); j++) {
            if (!colnames.get(j).equals(DBContract._ID)) {
                String entry = stripEnclosingDoubleQuotes(lineEntries.get(j));
                if (!entry.equals("")) {
                    values.put(colnames.get(j), entry);
                }
            }
        }
        return values;
    }

    public void importData(Uri uri){
        System.out.println("Importing...");

        try {
            InputStream inputStream = app.getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));

            DataManager dm = MyCapNutrition.dataManager;

            SparseArray<Integer> foodDBIDFromFileID = new SparseArray<>();
            SparseArray<Integer> unitDBIDFromFileID = new SparseArray<>();

            String table = reader.readLine();
            ArrayList<String> colnames = getEntriesFromLine(reader.readLine());
            int ncol = colnames.size();
            int idcol = colnames.indexOf(DBContract._ID);
            String line;
            ArrayList<String> lineEntries;
            while ((line = reader.readLine()) != null) {

                System.out.println(line);

                if (line.equals("")) {
                    if (!reader.ready()) {
                        break;
                    }
                    table = reader.readLine();
                    if (!reader.ready()) {
                        break;
                    }
                    colnames = getEntriesFromLine(reader.readLine());
                    ncol = colnames.size();
                    idcol = colnames.indexOf(DBContract._ID);
                } else {
                    lineEntries = getEntriesFromLine(line);

                    // Replace IDs in file with database IDs
                    if (table.equals(DBContract.UnitEntry.TABLE_NAME)) {
                        int foodDBIDCol = Integer.parseInt(lineEntries.get(
                                colnames.indexOf(DBContract.UnitEntry.COLUMN_NAME_FOOD_ID)));
                        int foodFileID = Integer.parseInt(lineEntries.get(foodDBIDCol));
                        lineEntries.set(foodDBIDCol,
                                dm.getFood(foodDBIDFromFileID.get(foodFileID)).DBID + "");
                    } else if (table.equals(DBContract.RecordEntry.TABLE_NAME)) {
                        int foodDBIDCol = colnames.indexOf(DBContract.RecordEntry.COLUMN_NAME_FOOD_ID);
                        System.out.println("Record foodDBIDCol = " + foodDBIDCol);
                        int foodFileID = Integer.parseInt(lineEntries.get(foodDBIDCol));
                        System.out.println("Record foodFileID = " + foodFileID);
                        Food food = dm.getFood(foodDBIDFromFileID.get(foodFileID));
                        System.out.println("Got food " + food.name);
                        lineEntries.set(foodDBIDCol, food.DBID + "");
                        System.out.println("New ID: " + lineEntries.get(foodDBIDCol));

                        int unitDBIDCol = colnames.indexOf(DBContract.RecordEntry.COLUMN_NAME_UNIT_ID);
                        System.out.println("unitDBIDCol: " + unitDBIDCol);
                        System.out.println("raw: " + lineEntries.get(foodDBIDCol));
                        if (!lineEntries.get(unitDBIDCol).equals("")) {
                            int unitFileID = Integer.parseInt(lineEntries.get(unitDBIDCol));
                            System.out.println("unitFileID: " + unitFileID);
                            lineEntries.set(unitDBIDCol,
                                    dm.getFood(unitDBIDFromFileID.get(unitFileID)).DBID + "");
                        }
                    }

                    // Write to database
                    System.out.println("Importing record; columns: " + ncol);
                    for (int j = 0; j < ncol; j++) {
                        System.out.println(lineEntries.get(j));
                    }
                    int fileID = Integer.parseInt(lineEntries.get(idcol));
                    ContentValues values = contentValuesFromLineEntries(colnames, lineEntries);
                    int dbid = dm.directInsert(table, values);

                    // Update maps from file IDs to database IDs
                    if (table.equals(DBContract.FoodEntry.TABLE_NAME)) {
                        System.out.println("Food " + fileID + " maps to " + dbid);
                        foodDBIDFromFileID.put(fileID, dbid);
                    } else if (table.equals(DBContract.UnitEntry.TABLE_NAME)) {
                        System.out.println("Unit " + fileID + " maps to " + dbid);
                        unitDBIDFromFileID.put(fileID, dbid);
                    }
                }
            }
            inputStream.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
