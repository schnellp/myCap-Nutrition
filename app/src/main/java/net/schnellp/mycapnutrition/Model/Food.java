package net.schnellp.mycapnutrition.Model;

import android.content.ContentValues;

import net.schnellp.mycapnutrition.Model.DBContract.FoodEntry;

public class Food extends DataObject {

    public Food(int dbid, ContentValues values) {
        super(dbid, values);
    }

    public String getName() {
        return values.getAsString(FoodEntry.COLUMN_NAME_NAME);
    }

    public IntOrNA getReferenceServing_mg() {
        return new IntOrNA(values.getAsString(FoodEntry.COLUMN_NAME_REF_SERVING_MG));
    }

    public IntOrNA getKcal() {
        return new IntOrNA(values.getAsString(FoodEntry.COLUMN_NAME_KCAL));
    }

    public IntOrNA getCarb_mg() {
        return new IntOrNA(values.getAsString(FoodEntry.COLUMN_NAME_CARB_MG));
    }

    public IntOrNA getFat_mg() {
        return new IntOrNA(values.getAsString(FoodEntry.COLUMN_NAME_FAT_MG));
    }

    public IntOrNA getProtein_mg() {
        return new IntOrNA(values.getAsString(FoodEntry.COLUMN_NAME_PROTEIN_MG));
    }

    public IntOrNA getServings() {
        return new IntOrNA(values.getAsString(FoodEntry.COLUMN_NAME_RECIPE_SERVINGS));
    }
}
