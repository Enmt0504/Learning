package com.learning.enmt0504.favoriteapps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
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
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by enomoto on 17/06/21.
 */

public class MainFragment extends BrowseFragment {
    private static final String TAG = "MainFragment";

    private static int REQUEST_CODE = 123;

    private ArrayObjectAdapter mRowsAdapter;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private BackgroundManager mBackgroundManager;
    private List<InstalledApp> mList;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        prepareBackgroundManager();

        setupUIElements();

        loadRows();

        setFavorite();

        setupEventListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == DetailsActivity.FAVORITE_CHANGED) {
                setFavorite();
            }
        }
    }

    private void loadRows() {
        mList = InstalledAppList.setupApps(getActivity());

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        ArrayObjectAdapter listRowAdapterApps = new ArrayObjectAdapter(new CardPresenter());
        for (InstalledApp installedApp : mList) {
            listRowAdapterApps.add(installedApp);
        }
        HeaderItem header = new HeaderItem(1, InstalledAppList.CATEGORY[1]);
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

    private void setFavorite() {
        SharedPreferences sp = getContext().getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);

        Map<String, ?> map = sp.getAll();
        List<Pair<String, Long>> list = new ArrayList<>();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            list.add(new Pair<>(entry.getKey(), (Long) entry.getValue()));
        }

        Collections.sort(list, new Comparator<Pair<String, Long>>() {
            @Override
            public int compare(Pair<String, Long> stringLongPair, Pair<String, Long> t1) {
                return stringLongPair.second.compareTo(t1.second);
            }
        });

        ArrayObjectAdapter listRowAdapterFavorite = new ArrayObjectAdapter(new CardPresenter());
        for (Pair<String, Long> pair : list) {
            for (InstalledApp installedApp : mList) {
                if (pair.first.equals(installedApp.getPackageName())) {
                    listRowAdapterFavorite.add(installedApp);
                }
            }
        }

        HeaderItem header = new HeaderItem(0, InstalledAppList.CATEGORY[0]);
        if (mRowsAdapter.size() < InstalledAppList.CATEGORY.length) {
            mRowsAdapter.add(0, new ListRow(header, listRowAdapterFavorite));
        } else {
            mRowsAdapter.replace(0, new ListRow(header, listRowAdapterFavorite));
        }
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
                startActivityForResult(intent, REQUEST_CODE);
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
