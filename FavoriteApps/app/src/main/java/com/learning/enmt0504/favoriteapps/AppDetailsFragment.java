package com.learning.enmt0504.favoriteapps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    public enum AppAction {
        START_APP(1, R.string.start_app),
        ADD_FAVORITE(2, R.string.add_favorite),
        REMOVE_FAVORITE(3, R.string.remove_favorite);

        private long id;
        private final int strId;

        private AppAction(final long id, final int strId) {
            this.id = id;
            this.strId = strId;
        }

        public long getId() {
            return id;
        }

        public int getStrId() {
            return strId;
        }
    }

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

        getActivity().setResult(DetailsActivity.FAVORITE_UNCHANGED);

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

        row.addAction(new Action(AppAction.START_APP.getId(),
                getResources().getString(AppAction.START_APP.getStrId())));

        SharedPreferences sp = getContext().getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        if (sp.getLong(mSelectedApp.getPackageName(), -1) != -1) {
            row.addAction(new Action(AppAction.REMOVE_FAVORITE.getId(),
                    getResources().getString(AppAction.REMOVE_FAVORITE.getStrId())));
        } else {
            row.addAction(new Action(AppAction.ADD_FAVORITE.getId(),
                    getResources().getString(AppAction.ADD_FAVORITE.getStrId())));
        }

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
                if (action.getId() == AppAction.START_APP.getId()) {
                    Intent intent = new Intent();
                    intent.setClassName(mSelectedApp.getPackageName(), mSelectedApp.getActivityName());
                    startActivity(intent);
                } else {
                    updateFavorite(action.getId());
                }
            }
        });
        mPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsPresenter);
    }

    private void updateFavorite(long id) {
        SharedPreferences sp = getContext().getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();


        if (id == AppAction.ADD_FAVORITE.getId()) {
            editor.putLong(mSelectedApp.getPackageName(), System.currentTimeMillis());
            if (editor.commit() == true) {
                Toast.makeText(getActivity(), getResources().getString(R.string.add_favorite),
                        Toast.LENGTH_SHORT).show();
                getActivity().setResult(DetailsActivity.FAVORITE_CHANGED);
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.failed),
                        Toast.LENGTH_SHORT).show();
            }
        } else if (id == AppAction.REMOVE_FAVORITE.getId()) {
            editor.remove(mSelectedApp.getPackageName());
            if (editor.commit() == true) {
                Toast.makeText(getActivity(), getResources().getString(R.string.remove_favorite),
                        Toast.LENGTH_SHORT).show();
                getActivity().setResult(DetailsActivity.FAVORITE_CHANGED);
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.failed),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}