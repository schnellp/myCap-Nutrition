package net.schnellp.mycapnutrition.View.MultiSelectListView;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class MultiSelectAdapter<T> extends BaseAdapter {

    protected static class CheckableObject<T> {
        public final T object;
        public boolean isChecked = false;

        public CheckableObject(T object) {
            this.object = object;
        }
    }

    protected ArrayList<CheckableObject<T>> items = new ArrayList<>();

    public void addAll(List<T> list) {
        for (T item : list) {
            items.add(new CheckableObject<T>(item));
        }
    }

    public int getNumChecked() {
        int numChecked = 0;
        for (CheckableObject item : items) {
            if (item.isChecked) {
                numChecked++;
            }
        }
        return numChecked;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return getTypedItem(position);
    }

    public T getTypedItem(int position) {
        return (T) items.get(position).object;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setItemChecked(int position, boolean checked) {
        items.get(position).isChecked = checked;
    }

    public boolean isItemChecked(int position) {
        return items.get(position).isChecked;
    }

    public ArrayList<T> getCheckedItems() {
        ArrayList<T> checkedItems = new ArrayList<>();
        for (int i : getCheckedPositions()) {
            checkedItems.add(items.get(i).object);
        }
        return checkedItems;
    }

    public ArrayList<Integer> getCheckedPositions() {
        ArrayList<Integer> checkedPositions = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isChecked) {
                checkedPositions.add(i);
            }
        }
        return checkedPositions;
    }
}
