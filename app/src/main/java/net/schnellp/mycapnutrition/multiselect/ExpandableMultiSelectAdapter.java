package net.schnellp.mycapnutrition.multiselect;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

public abstract class ExpandableMultiSelectAdapter<T> extends BaseExpandableListAdapter
    implements ExpandableListView.OnGroupClickListener, AdapterView.OnItemLongClickListener {

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

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return getTypedGroup(groupPosition);
    }

    public T getTypedGroup(int groupPosition) {
        return items.get(groupPosition).object;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public void showMenuGroupIfApplicable(Context context) {
        ((MultiSelectActivity) context).setMultiSelectOptionsMenuVisible(getNumChecked() > 0);
        ((MultiSelectActivity) context).setSingleSelectOptionsMenuVisible(getNumChecked() == 1);
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View view, int position, long id) {
        if (getNumChecked() > 0) {
            if (((ActivatedLinearLayout) view).isChecked()) {
                ((ActivatedLinearLayout) view).setChecked(false);
                setItemChecked(position, false);
            } else {
                ((ActivatedLinearLayout) view).setChecked(true);
                setItemChecked(position, true);
            }
            showMenuGroupIfApplicable(view.getContext());
            return true;
        }
        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        for (int i = 0; i < getGroupCount(); i++) {
            ((ExpandableListView) parent).collapseGroup(i);
        }

        if (((ActivatedLinearLayout) view).isChecked()) {
            ((ActivatedLinearLayout) view).setChecked(false);
            setItemChecked(position, false);
        } else {
            ((ActivatedLinearLayout) view).setChecked(true);
            setItemChecked(position, true);
        }
        showMenuGroupIfApplicable(view.getContext());
        return true;
    }
}
