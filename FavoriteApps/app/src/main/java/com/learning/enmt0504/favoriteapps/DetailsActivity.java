package com.learning.enmt0504.favoriteapps;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by enomoto on 17/06/22.
 */

public class DetailsActivity extends Activity {
    public static final String APP = "App";
    public static final int FAVORITE_UNCHANGED = 0;
    public static final int FAVORITE_CHANGED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }
}
