package com.learning.enmt0504.favoriteapps;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by enomoto on 17/06/21.
 */

public class InstalledApp implements Serializable {
    private String label;
    private Drawable icon;
    private boolean favorite;

    public InstalledApp() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
