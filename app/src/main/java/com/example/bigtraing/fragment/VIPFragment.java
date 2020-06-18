package com.example.bigtraing.fragment;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bigtraing.R;
import com.example.bigtraing.adapter.VIPRvAdapter;
import com.example.bigtraing.base.BaseMvpFragment;
import com.example.bigtraing.model.CourseModel;
import com.example.frame.ApiConfig;
import com.example.frame.ICommonModel;
import com.example.frame.LoadTypeConfig;
import com.example.frame.bean.VIPBannerBean;
import com.example.frame.bean.VIPBottomDataBean;
import com.example.frame.utils.ParamHashMap;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class VIPFragment extends BaseMvpFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int page = 1;
    private ParamHashMap map;
    private String specialty_id="1";
    private List<String> imgs = new ArrayList<>();
    private VIPRvAdapter adapter;
    private Serializable dataBean;

    @Override
    public void setData() {
        initBannerData();
        initBottomData();
    }

    @Override
    public void setView() {
//        if (SharedPrefrenceUtils.getObject(getActivity(), ConstantKey.Constan) != null) {
//            dataBean = SharedPrefrenceUtils.getObject(getActivity(), ConstantKey.Constan);
//            specialty_id = dataBean.getSpecialty_id();
//        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new VIPRvAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                map = new ParamHashMap().add("specialty_id", specialty_id).add("page", page);
                mPresenter.getData(ApiConfig.VIP_BOTTOM_DATA_INFO, LoadTypeConfig.MORE, map);

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                map = new ParamHashMap().add("specialty_id", specialty_id).add("page", page);
                mPresenter.getData(ApiConfig.VIP_BOTTOM_DATA_INFO, LoadTypeConfig.REFRESH, map);
            }
        });
    }

    @Override
    public ICommonModel setModel() {
        return new CourseModel();
    }


    @Override
    public int setLayoutId() {
        return R.layout.fragment_vip;
    }

    @Override
    public void netSuccess(int whichApi, Object[] pD) {
        switch (whichApi) {
            case ApiConfig.VIP_BANNER_DATA_INFO:
                if (((VIPBannerBean) pD[0]).getResult() != null) {
                    VIPBannerBean vipBannerBean = (VIPBannerBean) pD[0];
                    VIPBannerBean.ResultBean result = vipBannerBean.getResult();
                    List<VIPBannerBean.ResultBean.LiveBeanX.LiveBean> live = result.getLive().getLive();
                    adapter.initLive(live);
                    List<VIPBannerBean.ResultBean.LunbotuBean> lunbotu = result.getLunbotu();
                    for (VIPBannerBean.ResultBean.LunbotuBean lunbotuBean : lunbotu) {
                        imgs.add(lunbotuBean.getImg());
                    }
                    adapter.initBanner(imgs);

                }
                break;
            case ApiConfig.VIP_BOTTOM_DATA_INFO:
              int vip= (int) pD[1];
                if (((VIPBottomDataBean) pD[0]).getResult() != null) {
                    if (vip == LoadTypeConfig.MORE) {
                        refreshLayout.finishLoadMore();
                    } else if (vip == LoadTypeConfig.REFRESH) {
                        if (adapter.getList().size() > 0) {
                            adapter.getList().clear();
                        }
                        refreshLayout.finishRefresh();
                    }
                    VIPBottomDataBean vipBottomDataBean = (VIPBottomDataBean) pD[0];
                    List<VIPBottomDataBean.ResultBean.ListBean> list = vipBottomDataBean.getResult().getList();
                    adapter.initList(list);
                }
                break;
        }
    }


    private void initBottomData() {
        map = new ParamHashMap().add("specialty_id", specialty_id).add("page", page);
        mPresenter.getData(ApiConfig.VIP_BOTTOM_DATA_INFO, LoadTypeConfig.NORMAL, map);
    }

    private void initBannerData() {
        mPresenter.getData(ApiConfig.VIP_BANNER_DATA_INFO, LoadTypeConfig.NORMAL);
    }


}


