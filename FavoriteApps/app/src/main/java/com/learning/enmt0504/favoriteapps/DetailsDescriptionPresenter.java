package com.learning.enmt0504.favoriteapps;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

/**
 * Created by enomoto on 17/06/22.
 */

public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {
    @Override
    protected void onBindDescription(ViewHolder vh, Object item) {
        InstalledApp installedApp = (InstalledApp) item;

        if (installedApp != null) {
            vh.getTitle().setText(installedApp.getLabel());
        }
    }
}
