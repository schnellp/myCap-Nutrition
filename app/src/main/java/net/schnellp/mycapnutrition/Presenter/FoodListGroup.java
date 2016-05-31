package net.schnellp.mycapnutrition.Presenter;

import net.schnellp.mycapnutrition.Model.Record;

import java.util.ArrayList;
import java.util.List;

public class FoodListGroup {

    public final Record record;
    public final List<String> children = new ArrayList<>();

    public FoodListGroup(Record record) {
        this.record = record;
        children.add("placeholder");
    }
}
