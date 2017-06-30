package com.learning.enmt0504.favoriteapps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.widget.Toast;

/**
 * Created by enomoto on 17/06/22.
 */

public class AppDetailsFragment extends DetailsFragment {
    public enum AppAction {
        START_APP(1, R.string.start_app),
        ADD_FAVORITE(2, R.string.add_favorite),
        REMOVE_FAVORITE(3, R.string.remove_favorite);

        private final long id;
        private final int strId;

        AppAction(final long id, final int strId) {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    /**
     * Adapterの初期化を行う
     */
    private void setupAdapter() {
        mPresenterSelector = new ClassPresenterSelector();
        mAdapter = new ArrayObjectAdapter(mPresenterSelector);
        setAdapter(mAdapter);
    }

    /**
     * アプリの情報とボタンの表示
     */
    private void setupDetailsOverviewRow() {
        final DetailsOverviewRow row = new DetailsOverviewRow(mSelectedApp);
        row.setImageDrawable(getActivity().getDrawable(R.color.default_background));

        row.setImageDrawable(mSelectedApp.getBanner());

        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());

        row.addAction(new Action(AppAction.START_APP.getId(),
                getResources().getString(AppAction.START_APP.getStrId())));

        SharedPreferences sp = getContext().getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);

        // パッケージ名がFavoriteあるなら削除ボタンを,なければ追加ボタンを作る
        if (sp.getLong(mSelectedApp.getPackageName(), -1) != -1) {
            row.addAction(new Action(AppAction.REMOVE_FAVORITE.getId(),
                    getResources().getString(AppAction.REMOVE_FAVORITE.getStrId())));
        } else {
            row.addAction(new Action(AppAction.ADD_FAVORITE.getId(),
                    getResources().getString(AppAction.ADD_FAVORITE.getStrId())));
        }

        mAdapter.add(row);
    }

    /**
     * ボタンが押された際の動作の設定
     */
    private void setupDetailsOverviewRowPresenter() {
        FullWidthDetailsOverviewRowPresenter detailsPresenter =
                new FullWidthDetailsOverviewRowPresenter(new DetailsDescriptionPresenter());
        detailsPresenter.setBackgroundColor(getActivity().getColor(R.color.selected_background));

        // 起動が押されたら表示しているアプリを起動する
        // 起動以外が押されたらupdateFavoriteを呼ぶ
        detailsPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                if (action.getId() == AppAction.START_APP.getId()) {
                    Intent intent = new Intent();
                    intent.setClassName(mSelectedApp.getPackageName(), mSelectedApp.getActivityName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    updateFavorite(action.getId());
                }
            }
        });
        mPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsPresenter);
    }

    /**
     * SharedPreferencesに書かれたFavoriteの情報を更新する
     * 追加ならパッケージ名と追加された時間を書き込む
     * 削除ならそのパッケージ名の情報を削除する
     * @param id 追加/削除
     */
    private void updateFavorite(long id) {
        SharedPreferences sp = getContext().getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Intent data = new Intent();
        data.putExtra("packageName", mSelectedApp.getPackageName());

        if (id == AppAction.ADD_FAVORITE.getId()) {
            editor.putLong(mSelectedApp.getPackageName(), System.currentTimeMillis());
            if (editor.commit()) {
                Toast.makeText(getActivity(), getResources().getString(R.string.add_favorite),
                        Toast.LENGTH_SHORT).show();
                getActivity().setResult(DetailsActivity.FAVORITE_ADDED, data);
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.failed),
                        Toast.LENGTH_SHORT).show();
            }
        } else if (id == AppAction.REMOVE_FAVORITE.getId()) {
            editor.remove(mSelectedApp.getPackageName());
            if (editor.commit()) {
                Toast.makeText(getActivity(), getResources().getString(R.string.remove_favorite),
                        Toast.LENGTH_SHORT).show();
                getActivity().setResult(DetailsActivity.FAVORITE_REMOVED, data);
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.failed),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}