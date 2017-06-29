package com.learning.enmt0504.favoriteapps;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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

        PackageManager pm = activity.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> appList = pm.queryIntentActivities(intent, 0);
        intent.removeCategory(Intent.CATEGORY_LAUNCHER);
        intent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        appList.addAll(pm.queryIntentActivities(intent, 0));

        for (ResolveInfo info : appList) {
            String packageName = info.activityInfo.packageName;
            if (packageName.equals(activity.getPackageName())) {
                continue;
            }

            String label = info.loadLabel(pm).toString();
            String activityName = info.activityInfo.name;
            Drawable icon = info.loadIcon(pm);
            Drawable banner;
            try {
                banner = pm.getApplicationBanner(packageName);
            } catch (PackageManager.NameNotFoundException e) {
                // 存在するアプリケーションからpackageNameを持ってきているため,
                // 見つからないことはないはず.
                continue;
            }

            list.add(buildInstalledAppInfo(label, packageName, activityName, icon, banner));
        }

        return list;
    }

    private static InstalledApp buildInstalledAppInfo(String label, String packageName,
                                                      String activityName, Drawable icon,
                                                      Drawable banner) {
        InstalledApp installedApp = new InstalledApp();
        installedApp.setLabel(label);
        installedApp.setPackageName(packageName);
        installedApp.setActivityName(activityName);
        installedApp.setIcon(icon);
        installedApp.setBanner(banner);

        return installedApp;
    }
}
