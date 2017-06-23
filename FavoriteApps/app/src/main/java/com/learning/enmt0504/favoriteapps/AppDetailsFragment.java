package com.learning.enmt0504.favoriteapps;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.DetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by enomoto on 17/06/22.
 */

public class AppDetailsFragment extends DetailsFragment {
    private static final String TAG = "AppDetailsFragment";

    private static final int ACTION_START_APP = 1;
    private static final int ACTION_ADD_FAVORITE = 2;

    private InstalledApp mSelectedApp;

    private ArrayObjectAdapter mAdapter;
    private ClassPresenterSelector mPresenterSelector;

    private BackgroundManager mBackgroundManager;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate DetailsFragment");
        super.onCreate(savedInstanceState);

        prepareBackgroundManager();

        mSelectedApp = (InstalledApp) getActivity().getIntent()
                .getSerializableExtra(DetailsActivity.APP);
        if (mSelectedApp != null) {
            setupAdapter();
            setupDetailsOverviewRow();
            setupDetailsOverviewRowPresenter();
        } else {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mDefaultBackground = getResources().getDrawable(R.color.default_background);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupAdapter() {
        mPresenterSelector = new ClassPresenterSelector();
        mAdapter = new ArrayObjectAdapter(mPresenterSelector);
        setAdapter(mAdapter);
    }

    private void setupDetailsOverviewRow() {
        final DetailsOverviewRow row = new DetailsOverviewRow(mSelectedApp);
        row.setImageDrawable(getResources().getDrawable(R.color.default_background));

        row.setImageDrawable(mSelectedApp.getBanner());

        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());

        row.addAction(new Action(ACTION_START_APP, getResources().getString(R.string.start_app)));
        row.addAction(new Action(ACTION_ADD_FAVORITE, getResources().getString(R.string.add_favorite)));

        mAdapter.add(row);
    }

    private void setupDetailsOverviewRowPresenter() {
        DetailsOverviewRowPresenter detailsPresenter =
                new DetailsOverviewRowPresenter(new DetailsDescriptionPresenter());
        detailsPresenter.setBackgroundColor(getResources().getColor(R.color.selected_background));
        detailsPresenter.setStyleLarge(true);

        detailsPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                if (action.getId() == ACTION_START_APP) {
                    Intent intent = new Intent();
                    intent.setClassName(mSelectedApp.getPackageName(), mSelectedApp.getActivityName());
                    startActivity(intent);
                } else if (action.getId() == ACTION_ADD_FAVORITE) {
                    Toast.makeText(getActivity(), "add favorite", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsPresenter);
    }
}