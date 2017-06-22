package com.learning.enmt0504.favoriteapps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by enomoto on 17/06/21.
 */

public class InstalledApp implements Serializable {
    private String label;
    private String packageName;
    private byte[] icon;
    private boolean favorite;

    public InstalledApp() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        if (icon != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Drawable iconDrawable = new BitmapDrawable(BitmapFactory.decodeByteArray(icon, 0, icon.length, options));
            return iconDrawable;
        }

        return null;
    }

    public void setIcon(Drawable icon) {
        if (icon != null) {
            Bitmap bitmapIcon = ((BitmapDrawable) icon).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmapIcon.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            this.icon = bos.toByteArray();
        }
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
