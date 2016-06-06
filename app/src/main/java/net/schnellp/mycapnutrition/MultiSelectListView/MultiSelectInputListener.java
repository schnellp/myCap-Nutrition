package net.schnellp.mycapnutrition.MultiSelectListView;

import android.content.Context;
import android.view.View;

public class MultiSelectInputListener implements View.OnClickListener, View.OnLongClickListener {

    protected MultiSelectAdapter ms;
    protected int position;

    public MultiSelectInputListener(MultiSelectAdapter ms, int position) {
        this.ms = ms;
        this.position = position;
    }

    private void showMenuGroupIfApplicable(Context context) {
        ((MultiSelectActivity) context).setMultiSelectOptionsMenuVisible(ms.getNumChecked() > 0);
        ((MultiSelectActivity) context).setSingleSelectOptionsMenuVisible(ms.getNumChecked() == 1);
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
        showMenuGroupIfApplicable(v.getContext());
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
        showMenuGroupIfApplicable(v.getContext());
        return true;
    }
}
