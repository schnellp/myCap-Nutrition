package net.schnellp.mycapnutrition.multiselect;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class MultiSelectAdapter<T> extends BaseAdapter
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    //TODO: Dirty, ugly hack. Find better way to handle positioning when headers present.
    public int headerCount = 0;

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

    protected void showMenuGroupIfApplicable(Context context) {
        ((MultiSelectActivity) context).setMultiSelectOptionsMenuVisible(getNumChecked() > 0);
        ((MultiSelectActivity) context).setSingleSelectOptionsMenuVisible(getNumChecked() == 1);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < headerCount) {
            return;
        }
        if (getNumChecked() > 0) {
            if (((ActivatedLinearLayout) view).isChecked()) {
                ((ActivatedLinearLayout) view).setChecked(false);
                setItemChecked(position - headerCount, false);
            } else {
                ((ActivatedLinearLayout) view).setChecked(true);
                setItemChecked(position - headerCount, true);
            }
            showMenuGroupIfApplicable(view.getContext());
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < headerCount) {
            return false;
        }
        if (((ActivatedLinearLayout) view).isChecked()) {
            ((ActivatedLinearLayout) view).setChecked(false);
            setItemChecked(position - headerCount, false);
        } else {
            ((ActivatedLinearLayout) view).setChecked(true);
            setItemChecked(position - headerCount, true);
        }
        showMenuGroupIfApplicable(view.getContext());
        return true;
    }
}
