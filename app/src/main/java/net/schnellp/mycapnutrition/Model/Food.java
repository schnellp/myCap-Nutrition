package net.schnellp.mycapnutrition.Model;

import net.schnellp.mycapnutrition.Model.DBContract.FoodEntry;

public class Food extends DataObject {

    public Food(int dbid, String name, IntOrNA referenceServing_mg,
                IntOrNA kcal, IntOrNA carb_mg, IntOrNA fat_mg, IntOrNA protein_mg,
                boolean isRecipe, IntOrNA servings) {
        super(dbid);

        values.put(FoodEntry.COLUMN_NAME_NAME, name);

        if (!referenceServing_mg.isNA) {
            values.put(FoodEntry.COLUMN_NAME_REF_SERVING_MG, referenceServing_mg.val);
        }

        if (!kcal.isNA) {
            values.put(FoodEntry.COLUMN_NAME_KCAL, kcal.val);
        }

        if (!carb_mg.isNA) {
            values.put(FoodEntry.COLUMN_NAME_CARB_MG, carb_mg.val);
        }

        if (!fat_mg.isNA) {
            values.put(FoodEntry.COLUMN_NAME_FAT_MG, fat_mg.val);
        }

        if (!protein_mg.isNA) {
            values.put(FoodEntry.COLUMN_NAME_PROTEIN_MG, protein_mg.val);
        }

        if (!servings.isNA) {
            values.put(FoodEntry.COLUMN_NAME_RECIPE_SERVINGS, servings.val);
        }
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
