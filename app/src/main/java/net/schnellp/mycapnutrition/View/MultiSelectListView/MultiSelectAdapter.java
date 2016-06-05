package net.schnellp.mycapnutrition.View.MultiSelectListView;

public interface MultiSelectAdapter {

    int getNumChecked();

    void setItemChecked(int position, boolean checked);

    boolean isItemChecked(int position);
}
