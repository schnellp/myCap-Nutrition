package net.schnellp.mycapnutrition.Model;

import java.util.List;

public class ObjectMath {
    public static IntOrNA kcalSum(List<Record> records) {
        IntOrNA kcal = new IntOrNA(0, false);

        for (Record r : records) {
            kcal = kcal.add(r.kcal);
        }

        return kcal;
    }
}
