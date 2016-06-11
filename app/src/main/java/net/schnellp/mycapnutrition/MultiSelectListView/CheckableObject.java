package net.schnellp.mycapnutrition.MultiSelectListView;

public class CheckableObject<T> {
    public final T object;
    public boolean isChecked = false;

    public CheckableObject(T object) {
        this.object = object;
    }
}