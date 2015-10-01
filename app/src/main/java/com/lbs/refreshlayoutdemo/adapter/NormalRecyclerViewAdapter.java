package com.lbs.refreshlayoutdemo.adapter;

import android.content.Context;

import com.lbs.bgaadapter.BGARecyclerViewAdapter;
import com.lbs.bgaadapter.BGAViewHolderHelper;
import com.lbs.refreshlayoutdemo.R;
import com.lbs.refreshlayoutdemo.model.RefreshModel;


/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 16:31
 * 描述:
 */
public class NormalRecyclerViewAdapter extends BGARecyclerViewAdapter<RefreshModel> {
    public NormalRecyclerViewAdapter(Context context) {
        super(context, R.layout.item_normal);
    }

    @Override
    public void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        viewHolderHelper.setItemChildClickListener(R.id.tv_item_normal_delete);
        viewHolderHelper.setItemChildLongClickListener(R.id.tv_item_normal_delete);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, RefreshModel model) {
        viewHolderHelper.setText(R.id.tv_item_normal_title, model.title).setText(R.id.tv_item_normal_detail, model.detail);
    }
}