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
    private String activityName;
    private byte[] icon;
    private byte[] banner;
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

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
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

    public Drawable getBanner() {
        if (banner != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Drawable bannerDrawable = new BitmapDrawable(BitmapFactory.decodeByteArray(banner, 0, banner.length, options));
            return bannerDrawable;
        }

        return null;
    }

    public void setBanner(Drawable banner) {
        if (banner != null) {
            Bitmap bitmapBanner = ((BitmapDrawable) banner).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmapBanner.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            this.banner = bos.toByteArray();
        }
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "InstalledApp{" +
                "label='" + label + '\'' +
                ", packageName='" + packageName + '\'' +
                ", activityName='" + activityName + '\'' +
                "}";
    }
}
