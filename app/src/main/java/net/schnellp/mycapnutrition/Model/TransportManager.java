package net.schnellp.mycapnutrition.model;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
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

    public Uri exportData(Activity sender) {

        String dirPath = sender.getFilesDir().getAbsolutePath() + File.separator + "publicdata";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                throw new RuntimeException("Could not create directory " + dirPath);
            }
        }
        File file = new File(dir, "MyCap Nutrition data.mccsv");

        String[] tables = new String[] {
                DBContract.PackageEntry.TABLE_NAME,
                DBContract.FoodEntry.TABLE_NAME,
                DBContract.UnitEntry.TABLE_NAME,
                DBContract.IngredientEntry.TABLE_NAME,
                DBContract.RecordEntry.TABLE_NAME,
                DBContract.BodyMassEntry.TABLE_NAME
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

        Uri fileUri = null;

        try {
            fileUri = FileProvider.getUriForFile(
                    sender,
                    "net.schnellp.mycapnutrition.fileprovider",
                    file);
        } catch (IllegalArgumentException e) {
            Log.e("File Selector",
                    "The selected file can't be shared: " +
                            file.getAbsolutePath());
        }

        Intent shareIntent = new Intent();
        
        if (fileUri != null) {
            // Grant temporary read permission to the content URI
            shareIntent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Put the Uri and MIME type in the result Intent
            shareIntent.setDataAndType(
                    fileUri,
                    sender.getContentResolver().getType(fileUri));
        }


        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.setType("text/plain");
        sender.startActivity(Intent.createChooser(shareIntent, "Export to"));

        return Uri.fromFile(file);
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
                    if (inQuotes) {
                        builder.append(curChar);
                    } else {
                        entries.add(builder.toString());
                        builder.delete(0, builder.length());
                    }
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

        try {
            InputStream inputStream = app.getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                throw new IOException("Unable to create input stream.");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));

            DataManager dm = MyCapNutrition.dataManager;

            SparseArray<Integer> packageDBIDFromFileID = new SparseArray<>();
            SparseArray<Integer> foodDBIDFromFileID = new SparseArray<>();
            SparseArray<Integer> unitDBIDFromFileID = new SparseArray<>();

            String table = reader.readLine();
            ArrayList<String> colnames = getEntriesFromLine(reader.readLine());
            int idcol = colnames.indexOf(DBContract._ID);
            String line;
            ArrayList<String> lineEntries;
            while ((line = reader.readLine()) != null) {

                if (line.equals("")) {
                    if (!reader.ready()) {
                        break;
                    }
                    table = reader.readLine();
                    if (!reader.ready()) {
                        break;
                    }
                    colnames = getEntriesFromLine(reader.readLine());
                    idcol = colnames.indexOf(DBContract._ID);
                } else {
                    lineEntries = getEntriesFromLine(line);

                    // Replace IDs in file with database IDs
                    switch (table) {
                        case DBContract.FoodEntry.TABLE_NAME:

                            break;
                        case DBContract.UnitEntry.TABLE_NAME:
                            int foodDBIDCol = colnames.indexOf(DBContract.UnitEntry.COLUMN_NAME_FOOD_ID);
                            int foodFileID = Integer.parseInt(lineEntries.get(foodDBIDCol));
                            lineEntries.set(foodDBIDCol,
                                    dm.foodManager.get(foodDBIDFromFileID.get(foodFileID)).DBID + "");
                            break;
                        case DBContract.IngredientEntry.TABLE_NAME:
                            int recipeDBIDCol = colnames.indexOf(
                                    DBContract.IngredientEntry.COLUMN_NAME_RECIPE_ID);
                            int recipeFileID = Integer.parseInt(lineEntries.get(recipeDBIDCol));
                            lineEntries.set(recipeDBIDCol, "" + foodDBIDFromFileID.get(recipeFileID));

                            foodDBIDCol = colnames.indexOf(DBContract.IngredientEntry.COLUMN_NAME_FOOD_ID);
                            foodFileID = Integer.parseInt(lineEntries.get(foodDBIDCol));
                            lineEntries.set(foodDBIDCol, "" + foodDBIDFromFileID.get(foodFileID));

                            int unitDBIDCol = colnames.indexOf(DBContract.IngredientEntry.COLUMN_NAME_UNIT_ID);
                            if (!lineEntries.get(unitDBIDCol).equals("")) {
                                int unitFileID = Integer.parseInt(lineEntries.get(unitDBIDCol));
                                lineEntries.set(unitDBIDCol, "" + unitDBIDFromFileID.get(unitFileID));
                            }
                            break;
                        case DBContract.RecordEntry.TABLE_NAME:
                            foodDBIDCol = colnames.indexOf(DBContract.RecordEntry.COLUMN_NAME_FOOD_ID);
                            foodFileID = Integer.parseInt(lineEntries.get(foodDBIDCol));
                            lineEntries.set(foodDBIDCol, "" + foodDBIDFromFileID.get(foodFileID));

                            unitDBIDCol = colnames.indexOf(DBContract.RecordEntry.COLUMN_NAME_UNIT_ID);
                            if (!lineEntries.get(unitDBIDCol).equals("")) {
                                int unitFileID = Integer.parseInt(lineEntries.get(unitDBIDCol));
                                lineEntries.set(unitDBIDCol, "" + unitDBIDFromFileID.get(unitFileID));
                            }
                            break;
                    }

                    // Write to database
                    int fileID = Integer.parseInt(lineEntries.get(idcol));
                    ContentValues values = contentValuesFromLineEntries(colnames, lineEntries);
                    int dbid = dm.directInsert(table, values);

                    // Update maps from file IDs to database IDs
                    switch (table) {
                        case DBContract.PackageEntry.TABLE_NAME:
                            packageDBIDFromFileID.put(fileID, dbid);
                            break;
                        case DBContract.FoodEntry.TABLE_NAME:
                            foodDBIDFromFileID.put(fileID, dbid);
                            break;
                        case DBContract.UnitEntry.TABLE_NAME:
                            unitDBIDFromFileID.put(fileID, dbid);
                            break;
                    }
                    if (table.equals(DBContract.FoodEntry.TABLE_NAME)) {
                        foodDBIDFromFileID.put(fileID, dbid);
                    } else if (table.equals(DBContract.UnitEntry.TABLE_NAME)) {
                        unitDBIDFromFileID.put(fileID, dbid);
                    }
                }
            }
            inputStream.close();
            // MyCapNutrition.dataManager.rebuildFTS();
        } catch (IOException e) {
            Log.e("Exception","IOException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
        }
    }
}
