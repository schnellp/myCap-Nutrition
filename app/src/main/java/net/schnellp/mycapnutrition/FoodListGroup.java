package net.schnellp.mycapnutrition;

import net.schnellp.mycapnutrition.data.Record;

import java.util.ArrayList;
import java.util.List;

public class FoodListGroup {

    public final Record record;
    public final List<String> children = new ArrayList<String>();

    public FoodListGroup(Record record) {
        this.record = record;
    }
}
