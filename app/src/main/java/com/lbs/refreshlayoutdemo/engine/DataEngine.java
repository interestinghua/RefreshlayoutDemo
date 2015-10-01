package com.lbs.refreshlayoutdemo.engine;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lbs.bgabanner.BGABanner;
import com.lbs.refreshlayoutdemo.R;
import com.lbs.refreshlayoutdemo.model.BannerModel;
import com.lbs.refreshlayoutdemo.model.RefreshModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/26 上午1:03
 * 描述:
 */
public class DataEngine {
    private static AsyncHttpClient sAsyncHttpClient = new AsyncHttpClient();

    public static List<RefreshModel> loadInitDatas() {
        List<RefreshModel> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add(new RefreshModel("title" + i, "detail" + i));
        }
        return datas;
    }

    public static void loadNewData(final RefreshModelResponseHandler responseHandler) {
        sAsyncHttpClient.get("https://raw.githubusercontent.com/bingoogolapple/BGARefreshLayout-Android/server/api/newdata.json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                responseHandler.onFailure();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                List<RefreshModel> refreshModels = new GsonBuilder().create().fromJson(responseString, new TypeToken<ArrayList<RefreshModel>>() {
                }.getType());
                responseHandler.onSuccess(refreshModels);
            }
        });
    }

    public static void loadMoreData(final RefreshModelResponseHandler responseHandler) {
        sAsyncHttpClient.get("https://raw.githubusercontent.com/bingoogolapple/BGARefreshLayout-Android/server/api/moredata.json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                responseHandler.onFailure();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                List<RefreshModel> refreshModels = new GsonBuilder().create().fromJson(responseString, new TypeToken<ArrayList<RefreshModel>>() {
                }.getType());
                responseHandler.onSuccess(refreshModels);
            }
        });
    }

    public interface RefreshModelResponseHandler {
        void onFailure();
        void onSuccess(List<RefreshModel> refreshModels);
    }

    public static View getCustomHeaderView(Context context) {
        View headerView = View.inflate(context, R.layout.view_custom_header, null);
        final BGABanner banner = (BGABanner) headerView.findViewById(R.id.banner);
        final List<View> views = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            views.add(View.inflate(context, R.layout.view_image, null));
        }
        banner.setViews(views);
        sAsyncHttpClient.get("https://raw.githubusercontent.com/bingoogolapple/BGABanner-Android/server/api/5item.json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                BannerModel bannerModel = new Gson().fromJson(responseString, BannerModel.class);
                SimpleDraweeView simpleDraweeView;
                for (int i = 0; i < views.size(); i++) {
                    simpleDraweeView = (SimpleDraweeView) views.get(i);
                    simpleDraweeView.setImageURI(Uri.parse(bannerModel.imgs.get(i)));
                }
                banner.setTips(bannerModel.tips);
            }
        });
        return headerView;
    }

}