package com.lbs.refreshlayoutdemo.adapter;

import android.content.Context;

import com.lbs.bgaadapter.BGARecyclerViewAdapter;
import com.lbs.bgaadapter.BGAViewHolderHelper;
import com.lbs.bgaswipeitemlayout.BGASwipeItemLayout;
import com.lbs.refreshlayoutdemo.R;
import com.lbs.refreshlayoutdemo.model.RefreshModel;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 16:31
 * 描述:
 */
public class BGASwipeRecyclerViewAdapter extends BGARecyclerViewAdapter<RefreshModel> {
    private BGASwipeItemLayout mOpenedSil;

    public BGASwipeRecyclerViewAdapter(Context context) {
        super(context, R.layout.item_swipe);
    }

    @Override
    public void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        BGASwipeItemLayout swipeItemLayout = viewHolderHelper.getView(R.id.sil_item_swipe_root);
        swipeItemLayout.setDelegate(new BGASwipeItemLayout.BGASwipeItemLayoutDelegate() {
            @Override
            public void onBGASwipeItemLayoutOpened(BGASwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
                mOpenedSil = swipeItemLayout;
            }

            @Override
            public void onBGASwipeItemLayoutClosed(BGASwipeItemLayout swipeItemLayout) {
                mOpenedSil = null;
            }
        });
        viewHolderHelper.setItemChildClickListener(R.id.tv_item_swipe_delete);
        viewHolderHelper.setItemChildLongClickListener(R.id.tv_item_swipe_delete);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, RefreshModel model) {
        closeOpenedSwipeItemLayoutWithAnim();
        viewHolderHelper.setText(R.id.tv_item_swipe_title, model.title).setText(R.id.tv_item_swipe_detail, model.detail).setText(R.id.et_item_swipe_title, model.title);
    }

    public void closeOpenedSwipeItemLayoutWithAnim() {
        if (mOpenedSil != null) {
            mOpenedSil.closeWithAnim();
            mOpenedSil = null;
        }
    }

}