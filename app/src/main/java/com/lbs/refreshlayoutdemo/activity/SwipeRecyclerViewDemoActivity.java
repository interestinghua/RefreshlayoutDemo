package com.lbs.refreshlayoutdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.lbs.bgaadapter.BGAOnItemChildClickListener;
import com.lbs.bgaadapter.BGAOnItemChildLongClickListener;
import com.lbs.bgaadapter.BGAOnRVItemClickListener;
import com.lbs.bgaadapter.BGAOnRVItemLongClickListener;
import com.lbs.bgarefreshlayout.BGAMoocStyleRefreshViewHolder;
import com.lbs.bgarefreshlayout.BGARefreshLayout;
import com.lbs.refreshlayoutdemo.R;
import com.lbs.refreshlayoutdemo.adapter.BGASwipeRecyclerViewAdapter;
import com.lbs.refreshlayoutdemo.engine.DataEngine;
import com.lbs.refreshlayoutdemo.model.RefreshModel;
import com.lbs.refreshlayoutdemo.widget.Divider;

import java.util.List;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 10:06
 * 描述:
 */
public class SwipeRecyclerViewDemoActivity extends AppCompatActivity implements
        BGARefreshLayout.BGARefreshLayoutDelegate,
        BGAOnRVItemClickListener,
        BGAOnRVItemLongClickListener,
        BGAOnItemChildClickListener,
        BGAOnItemChildLongClickListener {

    private BGASwipeRecyclerViewAdapter mAdapter;
    private BGARefreshLayout mRefreshLayout;
    private List<RefreshModel> mDatas;
    private RecyclerView mDataRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        initRefreshLayout();
        initRecyclerView();
    }

    private void initRefreshLayout() {
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_recyclerview_refresh);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setCustomHeaderView(DataEngine.getCustomHeaderView(this), false);
        mRefreshLayout.setRefreshViewHolder(new BGAMoocStyleRefreshViewHolder(this, true));
    }

    private void initRecyclerView() {
        mDataRv = (RecyclerView) findViewById(R.id.rv_recyclerview_data);
        mDataRv.addItemDecoration(new Divider(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataRv.setLayoutManager(linearLayoutManager);

        mAdapter = new BGASwipeRecyclerViewAdapter(this);
        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnRVItemLongClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);

        mDatas = DataEngine.loadInitDatas();
        mAdapter.setDatas(mDatas);
        mDataRv.setAdapter(mAdapter);
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
    public void onRVItemClick(View v, int position) {
        Toast.makeText(this, "点击了条目 " + mAdapter.getItem(position).title, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onRVItemLongClick(View v, int position) {
        Toast.makeText(this, "长按了条目 " + mAdapter.getItem(position).title, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onItemChildClick(View v, int position) {
        if (v.getId() == R.id.tv_item_swipe_delete) {
            mAdapter.removeItem(position);
        }
    }

    @Override
    public boolean onItemChildLongClick(View v, int position) {
        if (v.getId() == R.id.tv_item_swipe_delete) {
            Toast.makeText(this, "长按了删除 " + mAdapter.getItem(position).title, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}