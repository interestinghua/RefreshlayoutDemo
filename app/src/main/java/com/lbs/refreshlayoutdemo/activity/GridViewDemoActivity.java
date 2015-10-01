package com.lbs.refreshlayoutdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
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
public class GridViewDemoActivity extends AppCompatActivity implements BGARefreshLayout.BGARefreshLayoutDelegate,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        BGAOnItemChildClickListener,
        BGAOnItemChildLongClickListener {

    private static final String TAG = GridViewDemoActivity.class.getSimpleName();
    private BGARefreshLayout mRefreshLayout;
    private List<RefreshModel> mDatas;
    private GridView mDataGv;
    private NormalAdapterViewAdapter mAdapter;

    private boolean mIsNetworkEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);

        initRefreshLayout();
        initListView();
    }

    private void initRefreshLayout() {
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_gridview_refresh);
        mRefreshLayout.setDelegate(this);
//        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(this, true));
        mRefreshLayout.setRefreshViewHolder(new BGAMoocStyleRefreshViewHolder(this, true));
//        mRefreshLayout.setRefreshViewHolder(new BGAStickinessRefreshViewHolder(this, true));
    }

    private void initListView() {
        mDataGv = (GridView) findViewById(R.id.lv_gridview_data);
        mDataGv.setOnItemClickListener(this);
        mDataGv.setOnItemLongClickListener(this);

        mAdapter = new NormalAdapterViewAdapter(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);

        mDatas = DataEngine.loadInitDatas();
        mAdapter.setDatas(mDatas);
        mDataGv.setAdapter(mAdapter);

        mDataGv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                Log.i(TAG, "滚动状态变化");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                Log.i(TAG, "正在滚动");
            }
        });

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        Log.i(TAG, "开始刷新");
        if (mIsNetworkEnabled) {
            // 如果网络可用，则加载网络数据
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
        } else {
            // 网络不可用，结束下拉刷新
            Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
            mRefreshLayout.endRefreshing();
        }
        // 模拟网络可用不可用
        mIsNetworkEnabled = !mIsNetworkEnabled;
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        Log.i(TAG, "开始加载更多");

        if (mIsNetworkEnabled) {
            // 如果网络可用，则异步加载网络数据，并返回true，显示正在加载更多
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
            // 模拟网络可用不可用
            mIsNetworkEnabled = !mIsNetworkEnabled;
            return true;
        } else {
            // 模拟网络可用不可用
            mIsNetworkEnabled = !mIsNetworkEnabled;

            // 网络不可用，返回false，不显示正在加载更多
            Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
            return false;
        }
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

    public void beginRefreshing(View v) {
        mRefreshLayout.beginRefreshing();
    }

    public void beginLoadingMore(View v) {
        mRefreshLayout.beginLoadingMore();
    }
}