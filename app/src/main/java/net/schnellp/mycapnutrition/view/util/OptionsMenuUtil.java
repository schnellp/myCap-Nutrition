package net.schnellp.mycapnutrition.view.util;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import net.schnellp.mycapnutrition.R;

public class OptionsMenuUtil {
    public static void tintMenuItems(Context context, Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            Drawable icon = item.getIcon();
            if (icon != null) {
                Drawable newIcon = item.getIcon();
                newIcon.mutate().setColorFilter(
                        context.getResources().getColor(R.color.colorPrimaryContrast),
                        PorterDuff.Mode.SRC_IN);
                item.setIcon(newIcon);
            }
        }
    }
}
