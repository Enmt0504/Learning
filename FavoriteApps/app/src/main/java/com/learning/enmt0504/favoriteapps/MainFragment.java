package com.learning.enmt0504.favoriteapps;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by enomoto on 17/06/21.
 */

public class MainFragment extends BrowseFragment {
    private static final String TAG = "MainFragment";

    private ArrayObjectAdapter mRowsAdapter;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private BackgroundManager mBackgroundManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        prepareBackgroundManager();

        setupUIElements();
    }

    @Override
    public void onStart() {
        super.onStart();

        loadRows();

        setupEventListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    private void loadRows() {
        List<InstalledApp> list = InstalledAppList.setupApps(getActivity());

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        SharedPreferences sp = getContext().getSharedPreferences("favorite", Context.MODE_PRIVATE);

        ArrayObjectAdapter listRowAdapterFavorite = new ArrayObjectAdapter(new CardPresenter());
        ArrayObjectAdapter listRowAdapterApps = new ArrayObjectAdapter(new CardPresenter());
        for (int i = 0; i < list.size(); i++) {
            InstalledApp installedApp = list.get(i);
            listRowAdapterApps.add(installedApp);
            if (sp.getBoolean(installedApp.getPackageName(), false) == true) {
                listRowAdapterFavorite.add(installedApp);
            }
        }
        HeaderItem header = new HeaderItem(0, InstalledAppList.CATEGORY[0]);
        mRowsAdapter.add(new ListRow(header, listRowAdapterFavorite));
        header = new HeaderItem(1, InstalledAppList.CATEGORY[1]);
        mRowsAdapter.add(new ListRow(header, listRowAdapterApps));

        setAdapter(mRowsAdapter);

    }

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mDefaultBackground = getResources().getDrawable(R.color.default_background);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupUIElements() {
        setTitle(getString(R.string.browse_title));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(getResources().getColor(R.color.fastlane_background));
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof InstalledApp) {
                InstalledApp installedApp = (InstalledApp) item;
                Log.d(TAG, "Item:"+item.toString());
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.APP, installedApp);

                Log.d(TAG, "startActivity");
                getActivity().startActivity(intent);
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof InstalledApp) {
            }
        }
    }
}
