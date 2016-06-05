package net.schnellp.mycapnutrition.View.MultiSelectListView;

import android.view.View;

public class MultiSelectInputListener implements View.OnClickListener, View.OnLongClickListener {

    private MultiSelect ms;

    public MultiSelectInputListener(MultiSelect ms) {
        this.ms = ms;
    }

    @Override
    public void onClick(View v) {
        if (ms.getNumChecked() > 0) {
            if (((ActivatedLinearLayout) v).isChecked()) {
                ((ActivatedLinearLayout) v).setChecked(false);
                ms.setNumChecked(ms.getNumChecked() - 1);
            } else {
                ((ActivatedLinearLayout) v).setChecked(true);
                ms.setNumChecked(ms.getNumChecked() + 1);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (((ActivatedLinearLayout) v).isChecked()) {
            ((ActivatedLinearLayout) v).setChecked(false);
            ms.setNumChecked(ms.getNumChecked() - 1);
        } else {
            ((ActivatedLinearLayout) v).setChecked(true);
            ms.setNumChecked(ms.getNumChecked() + 1);
        }
        return true;
    }
}
