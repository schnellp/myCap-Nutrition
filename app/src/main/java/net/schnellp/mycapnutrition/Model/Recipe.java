package net.schnellp.mycapnutrition.Model;

public class Recipe extends Food {
    public Recipe(int DBID, String name, IntOrNA referenceServing_mg,
                IntOrNA kcal, IntOrNA carb_mg, IntOrNA fat_mg, IntOrNA protein_mg) {
        super(DBID, name, referenceServing_mg,
                kcal, carb_mg, fat_mg, protein_mg);
    }
}
