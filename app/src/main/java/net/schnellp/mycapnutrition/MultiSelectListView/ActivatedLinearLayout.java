package net.schnellp.mycapnutrition.MultiSelectListView;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

import net.schnellp.mycapnutrition.R;

public class ActivatedLinearLayout extends LinearLayout implements Checkable {

    // public static final int[] CHECKED_STATE = {R.attr.state_checked};
    private boolean mChecked;

    public ActivatedLinearLayout(Context context) {
        super(context);
    }

    public ActivatedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActivatedLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setChecked(boolean b) {
        mChecked = b;
        refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        mChecked = !mChecked;
        refreshDrawableState();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] states =  super.onCreateDrawableState(extraSpace + 1);
        if (mChecked){
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.listItemChecked));
        } else {
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.listItemUnchecked));
        }
        return states;
    }
}
