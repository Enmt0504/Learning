package com.learning.enmt0504.favoriteapps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by enomoto on 17/06/21.
 */

public class MainFragment extends BrowseFragment {
    private static final String TAG = "MainFragment";

    private static final int REQUEST_CODE = 123;

    private ArrayObjectAdapter mRowsAdapter;
    private List<InstalledApp> mList;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        setupUIElements();

        loadRows();

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
            // Favoriteに変化があったら更新処理を呼ぶ
            if (resultCode != DetailsActivity.FAVORITE_UNCHANGED) {
                updateFavorite(resultCode, data.getStringExtra("packageName"));
            }
        }
    }

    /**
     * Favoriteとして表示するアプリの追加/削除を行う
     * @param resultCode 更新を種類(追加か削除)
     * @param packageName 追加/削除するアプリのパッケージ名
     */
    private void updateFavorite(int resultCode, String packageName) {
        for (InstalledApp installedApp : mList) {
            if (installedApp.getPackageName().equals(packageName)) {
                ArrayObjectAdapter adapter = ((ArrayObjectAdapter) ((ListRow) mRowsAdapter.get(0)).getAdapter());

                if (resultCode == DetailsActivity.FAVORITE_ADDED) {
                    adapter.add(installedApp);
                } else if (resultCode == DetailsActivity.FAVORITE_REMOVED) {
                    adapter.remove(installedApp);
                }
            }
        }
    }

    /**
     * 画面に表示するアプリの設定を行う
     */
    private void loadRows() {
        mList = InstalledAppList.setupApps(getActivity());

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        setFavorite();

        setApps();

        setAdapter(mRowsAdapter);
    }

    /**
     * インストール済みアプリ一覧をAdapterにセットする
     */
    private void setApps() {
        ArrayObjectAdapter listRowAdapterApps = new ArrayObjectAdapter(new CardPresenter());
        for (InstalledApp installedApp : mList) {
            listRowAdapterApps.add(installedApp);
        }
        HeaderItem header = new HeaderItem(1, InstalledAppList.CATEGORY[1]);
        mRowsAdapter.add(new ListRow(header, listRowAdapterApps));
    }

    /**
     * Favoriteに登録したアプリ一覧をAdapterにセットする
     * 順番は登録順
     */
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
        mRowsAdapter.add(new ListRow(header, listRowAdapterFavorite));
    }

    /**
     * UIの初期化を行う
     */
    private void setupUIElements() {
        setTitle(getString(R.string.browse_title));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(getActivity().getColor(R.color.fastlane_background));
    }

    /**
     * EventListenerの初期化を行う
     */
    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof InstalledApp) {
                // クリックされたアプリの詳細表示に遷移する
                InstalledApp installedApp = (InstalledApp) item;
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.APP, installedApp);

                startActivityForResult(intent, REQUEST_CODE);
            }
        }
    }
}
