package com.example.bigtraing.fragment;


import androidx.recyclerview.widget.RecyclerView;

import com.example.bigtraing.R;
import com.example.bigtraing.adapter.MainHomeAdapter;
import com.example.bigtraing.base.BaseMvpFragment;
import com.example.bigtraing.interfaces.DataListener;
import com.example.bigtraing.model.MainPageModel;
import com.example.frame.ApiConfig;
import com.example.frame.LoadTypeConfig;
import com.example.frame.bean.BannerLiveInfo;
import com.example.frame.bean.BaseInfo;
import com.example.frame.bean.IndexCommondEntity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainPageFragment extends BaseMvpFragment<MainPageModel> implements DataListener {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int currentPage = 1;

    private List<IndexCommondEntity> bottomList = new ArrayList<>();
    private List<String> bannerData = new ArrayList<>();
    private List<BannerLiveInfo.Live> liveData = new ArrayList<>();
    private MainHomeAdapter mAdapter;

    @Override
    public void setData() {
        mPresenter.getData(ApiConfig.MAIN_PAGE_LIST, LoadTypeConfig.NORMAL, currentPage);
        mPresenter.getData(ApiConfig.BANNER_LIVE, LoadTypeConfig.NORMAL);
    }

    @Override
    public void setView() {
        initRecyclerView(recyclerView, refreshLayout, this);
        mAdapter = new MainHomeAdapter(bottomList, bannerData, liveData, getActivity());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public MainPageModel setModel() {
        return new MainPageModel();
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_main_page;
    }


    private boolean mainList = false, banLive = false;

    @Override
    public void netSuccess(int whichApi, Object[] pD) {
        switch (whichApi) {
            case ApiConfig.MAIN_PAGE_LIST:
//                int loadMode = (int) ((Object[]) pD[1])[0];
                int loadMode = (int) pD[1];
                BaseInfo<List<IndexCommondEntity>> baseInfo = (BaseInfo<List<IndexCommondEntity>>) pD[0];
                if (baseInfo.isSuccess()) {
                    if (loadMode == LoadTypeConfig.MORE) refreshLayout.finishLoadMore();
                    if (loadMode == LoadTypeConfig.REFRESH) {
                        bottomList.clear();
                        refreshLayout.finishRefresh();
                    }
                    bottomList.addAll(baseInfo.result);
                    mainList = true;
                    if (banLive) {
                        mAdapter.notifyDataSetChanged();
                        mainList = false;
                    }
                } else showToast("列表加载错误");
                break;
            case ApiConfig.BANNER_LIVE:
                JsonObject jsonObject = (JsonObject) pD[0];
                try {
                    JSONObject object = new JSONObject(jsonObject.toString());
                    if (object.getString("errNo").equals("0")) {
//                        int load = (int) ((Object[]) pD[1])[0];
                        int load = (int) pD[1];
                        if (load == LoadTypeConfig.REFRESH) {
                            bannerData.clear();
                            liveData.clear();
                        }
                        String result = object.getString("result");
                        JSONObject resultObject = new JSONObject(result);
                        String live = resultObject.getString("live");
                        //如果该字段是以boolean类型返回的，有两种处理方式 1：写两个实体类，一个live类型是Boolean，一个是list 2：当这个字段为Boolean类型时，将其干掉
                        if (live.equals("true") || live.equals("false")) {
                            resultObject.remove("live");
                        }
                        result = resultObject.toString();
                        Gson gson = new Gson();
                        BannerLiveInfo info = gson.fromJson(result, BannerLiveInfo.class);
                        for (BannerLiveInfo.Carousel data : info.Carousel) {
                            bannerData.add(data.thumb);
                        }
                        if (info.live != null) liveData.addAll(info.live);
                        banLive = true;
                        if (mainList) {
                            mAdapter.notifyDataSetChanged();
                            banLive = false;
                        }
                    }
                } catch (JSONException pE) {
                    pE.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void dataType(int mode) {
        if (mode == LoadTypeConfig.REFRESH) {
            mainList = false;
            banLive = false;
            mPresenter.getData(ApiConfig.MAIN_PAGE_LIST, LoadTypeConfig.REFRESH, 1);
            mPresenter.getData(ApiConfig.BANNER_LIVE, LoadTypeConfig.REFRESH);
        } else {
            currentPage++;
            mPresenter.getData(ApiConfig.MAIN_PAGE_LIST, LoadTypeConfig.MORE, currentPage);
        }
    }
}
