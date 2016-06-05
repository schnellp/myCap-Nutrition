package net.schnellp.mycapnutrition.View.MultiSelectListView;

import android.content.Context;
import android.view.View;

public class MultiSelectInputListener implements View.OnClickListener, View.OnLongClickListener {

    private MultiSelectAdapter ms;

    public MultiSelectInputListener(MultiSelectAdapter ms) {
        this.ms = ms;
    }

    private void showMenuGroupIfApplicable(Context context) {
        ((MultiSelectActivity) context).setMultiSelectOptionsMenuVisible(ms.getNumChecked() > 0);
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
        showMenuGroupIfApplicable(v.getContext());
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
        showMenuGroupIfApplicable(v.getContext());
        return true;
    }
}
