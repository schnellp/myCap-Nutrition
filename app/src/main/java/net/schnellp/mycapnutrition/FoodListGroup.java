package net.schnellp.mycapnutrition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick on 5/23/2016.
 */
public class FoodListGroup {

    public String string;
    public final List<String> children = new ArrayList<String>();

    public FoodListGroup(String string) {
        this.string = string;
    }
}
