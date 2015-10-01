package com.lbs.refreshlayoutdemo.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lbs.bgaadapter.BGAOnItemChildClickListener;
import com.lbs.bgaadapter.BGAOnItemChildLongClickListener;
import com.lbs.bgarefreshlayout.BGAMoocStyleRefreshViewHolder;
import com.lbs.bgarefreshlayout.BGARefreshLayout;
import com.lbs.refreshlayoutdemo.R;
import com.lbs.refreshlayoutdemo.adapter.NormalAdapterViewAdapter;
import com.lbs.refreshlayoutdemo.engine.DataEngine;
import com.lbs.refreshlayoutdemo.model.RefreshModel;

import java.util.List;


/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 10:06
 * 描述:
 */
public class NormalListViewDemoActivity extends AppCompatActivity implements
        BGARefreshLayout.BGARefreshLayoutDelegate,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        BGAOnItemChildClickListener,
        BGAOnItemChildLongClickListener {

    private static final String TAG = NormalListViewDemoActivity.class.getSimpleName();
    private BGARefreshLayout mRefreshLayout;
    private List<RefreshModel> mDatas;
    private ListView mDataLv;
    private NormalAdapterViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        initRefreshLayout();
        initListView();
    }

    private void initRefreshLayout() {
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(this, true);
        moocStyleRefreshViewHolder.setUltimateColor(Color.rgb(0, 0, 255));
        moocStyleRefreshViewHolder.setOriginalBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.iqegg));
        moocStyleRefreshViewHolder.setLoadMoreBackgroundColorRes(R.color.custom_green);
        moocStyleRefreshViewHolder.setSpringDistanceScale(0.2f);
        moocStyleRefreshViewHolder.setRefreshViewBackgroundColorRes(R.color.custom_green);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);
        mRefreshLayout.setCustomHeaderView(DataEngine.getCustomHeaderView(this), true);
    }


    private void initListView() {
        mDataLv = (ListView) findViewById(R.id.lv_listview_data);
        mDataLv.setOnItemClickListener(this);
        mDataLv.setOnItemLongClickListener(this);

        mAdapter = new NormalAdapterViewAdapter(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);

        mDatas = DataEngine.loadInitDatas();
        mAdapter.setDatas(mDatas);
        mDataLv.setAdapter(mAdapter);

        mDataLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.i(TAG, "滚动状态变化");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i(TAG, "正在滚动");
            }
        });
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        DataEngine.loadNewData(new DataEngine.RefreshModelResponseHandler() {
            @Override
            public void onFailure() {
                mRefreshLayout.endRefreshing();
            }

            @Override
            public void onSuccess(List<RefreshModel> refreshModels) {
                mRefreshLayout.endRefreshing();
                mDatas.addAll(0, refreshModels);
                mAdapter.setDatas(mDatas);
            }
        });
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        DataEngine.loadMoreData(new DataEngine.RefreshModelResponseHandler() {
            @Override
            public void onFailure() {
                mRefreshLayout.endLoadingMore();
            }

            @Override
            public void onSuccess(List<RefreshModel> refreshModels) {
                mRefreshLayout.endLoadingMore();
                mAdapter.addDatas(refreshModels);
            }
        });
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "点击了条目 " + mAdapter.getItem(position).title, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "长按了" + mAdapter.getItem(position).title, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onItemChildClick(View v, int position) {
        if (v.getId() == R.id.tv_item_normal_delete) {
            mAdapter.removeItem(position);
        }
    }

    @Override
    public boolean onItemChildLongClick(View v, int position) {
        if (v.getId() == R.id.tv_item_normal_delete) {
            Toast.makeText(this, "长按了删除 " + mAdapter.getItem(position).title, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}