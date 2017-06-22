package com.learning.enmt0504.favoriteapps;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enomoto on 17/06/21.
 */

public class InstalledAppList {
    public static final String CATEGORY[] = {
            "Favorite",
            "Apps"
    };

    public static List<InstalledApp> list;

    public InstalledAppList() {
    }

    public static List<InstalledApp> setupApps(Activity activity) {
        list = new ArrayList<InstalledApp>();

        List<ApplicationInfo> applicationInfo = activity
                .getPackageManager()
                .getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo info : applicationInfo) {
            if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                continue;
            }
            if (info.packageName.equals(activity.getPackageName())) {
                continue;
            }

            PackageManager packageManager = activity.getPackageManager();
            String label = packageManager.getApplicationLabel(info).toString();
            Drawable icon = packageManager.getApplicationIcon(info);

            list.add(buildInstalledAppInfo(label, icon));
        }

        return list;
    }

    private static InstalledApp buildInstalledAppInfo(String label, Drawable icon) {
        InstalledApp installedApp = new InstalledApp();
        installedApp.setLabel(label);
        installedApp.setIcon(icon);
        installedApp.setFavorite(false);

        return installedApp;
    }
}
