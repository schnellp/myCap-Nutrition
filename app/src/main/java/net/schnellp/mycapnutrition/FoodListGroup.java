package net.schnellp.mycapnutrition;

import java.util.ArrayList;
import java.util.List;

public class FoodListGroup {

    public String string;
    public final List<String> children = new ArrayList<String>();

    public FoodListGroup(String string) {
        this.string = string;
    }
}
