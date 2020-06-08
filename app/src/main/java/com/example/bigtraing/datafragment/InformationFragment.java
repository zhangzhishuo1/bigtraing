package com.example.bigtraing.datafragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bigtraing.R;
import com.example.bigtraing.adapter.InformationAdapter;
import com.example.bigtraing.base.BaseMvpFragment;
import com.example.bigtraing.model.DataModel;
import com.example.data.InformationBean;
import com.example.data.SpecialtyChooseEntity;
import com.example.frame.ApiConfig;
import com.example.frame.constants.ConstantKey;
import com.example.frame.utils.ParamHashMap;
import com.example.utils.newAdd.SharedPrefrenceUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;



public class InformationFragment extends BaseMvpFragment<DataModel> {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private InformationAdapter informationAdapter;
    private int type=1;
    private ParamHashMap ma_p;
    private List<InformationBean.ResultBean> list;
    private int fid;

    @Override
    public void setUpData() {
        ma_p = new ParamHashMap().add("page", 1).add("type", type).add("fid", fid);
        mPresenter.getData(ApiConfig.GET_INFORMATION_INFO,ma_p);

    }

    @Override
    public void setUpView() {
        if (SharedPrefrenceUtils.getObject(getActivity(), ConstantKey.Constan)!=null){
            SpecialtyChooseEntity.DataBean bean = SharedPrefrenceUtils.getObject(getActivity(), ConstantKey.Constan);
            fid = bean.getFid();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                type++;
                ma_p = new ParamHashMap().add("page", 1).add("type", type).add("fid", fid);
                mPresenter.getData(ApiConfig.GET_INFORMATION_INFO,ma_p);

                refreshLayout.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                type=1;
                list.clear();
                ma_p = new ParamHashMap().add("page", 1).add("type", type).add("fid", fid);
                mPresenter.getData(ApiConfig.GET_INFORMATION_INFO,ma_p);
                refreshLayout.finishRefresh();
            }
        });
        list = new ArrayList<>();
        informationAdapter = new InformationAdapter(getActivity(), list);
        recyclerView.setAdapter(informationAdapter);

    }

    @Override
    public DataModel setModel() {
        return new DataModel();
    }

    @Override
    public int setLayoutId() {
        return R.layout.information_fragment;
    }

    @Override
    public void netSuccess(int whichApi, Object[] pD) {
        switch (whichApi){
            case  ApiConfig.GET_INFORMATION_INFO:
                InformationBean infor=(InformationBean) pD[0];
//                list.addAll(infor.getResult());
                informationAdapter.notifyDataSetChanged();
                break;
        }

    }
}
