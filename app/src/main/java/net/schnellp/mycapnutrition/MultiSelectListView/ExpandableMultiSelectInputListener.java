package net.schnellp.mycapnutrition.MultiSelectListView;

import android.view.View;

public class ExpandableMultiSelectInputListener implements View.OnClickListener, View.OnLongClickListener {

    protected ExpandableMultiSelectAdapter ms;
    protected int position;

    public ExpandableMultiSelectInputListener(ExpandableMultiSelectAdapter ms, int position) {
        this.ms = ms;
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        if (ms.getNumChecked() > 0) {
            if (((ActivatedLinearLayout) v).isChecked()) {
                ((ActivatedLinearLayout) v).setChecked(false);
                ms.setItemChecked(position, false);
            } else {
                ((ActivatedLinearLayout) v).setChecked(true);
                ms.setItemChecked(position, true);
            }
        }
        ms.showMenuGroupIfApplicable(v.getContext());
    }

    @Override
    public boolean onLongClick(View v) {
        if (((ActivatedLinearLayout) v).isChecked()) {
            ((ActivatedLinearLayout) v).setChecked(false);
            ms.setItemChecked(position, false);
        } else {
            ((ActivatedLinearLayout) v).setChecked(true);
            ms.setItemChecked(position, true);
        }
        ms.showMenuGroupIfApplicable(v.getContext());
        return true;
    }
}
