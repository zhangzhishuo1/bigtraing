package com.example.bigtraing.course_view;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bigtraing.R;
import com.example.bigtraing.adapter.PremiumAdapter;
import com.example.bigtraing.base.BaseMvpFragment;
import com.example.bigtraing.model.CouresModel;
import com.example.data.SpecialtyChooseEntity;
import com.example.data.TrainningBean;
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


public class PremiumFragment extends BaseMvpFragment<CouresModel> {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int page=1;
    private String specialty_id;
    private ParamHashMap map_o;
    private List<TrainningBean.ResultBean.ListsBean> lists;
    private PremiumAdapter premiumAdapter;
    private int course_type;

    public PremiumFragment(int course_type) {
        this.course_type = course_type;
    }


    @Override
    public CouresModel setModel() {
        return new CouresModel();
    }

    @Override
    public int setLayoutId() {
        return R.layout.premium_fragment;
    }

    @Override
    public void setUpView() {
        if (SharedPrefrenceUtils.getObject(getActivity(), ConstantKey.Constan)!=null){
            SpecialtyChooseEntity.DataBean bean = SharedPrefrenceUtils.getObject(getActivity(), ConstantKey.Constan);
            specialty_id = bean.getSpecialty_id();
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;

                map_o = new ParamHashMap().add("page", page).add("course_type", course_type)
                        .add("limit", 15).add("specialty_id", specialty_id)
                        .add("uid", 15063681).add("time", 1591366330);
                mPresenter.getData(ApiConfig.GET_TRAINING_INFO,map_o);
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                lists.clear();
                map_o = new ParamHashMap().add("page", page).add("course_type", course_type)
                        .add("limit", 15).add("specialty_id", specialty_id)
                        .add("uid", 15063681).add("time", 1591366330);
                mPresenter.getData(ApiConfig.GET_TRAINING_INFO,map_o);
                refreshLayout.finishRefresh();
            }
        });
        lists = new ArrayList<>();
        premiumAdapter = new PremiumAdapter(getActivity(),lists);
        recyclerView.setAdapter(premiumAdapter);

    }

    @Override
    public void setUpData() {

        map_o = new ParamHashMap().add("page", page).add("course_type", course_type)
                .add("limit", 15).add("specialty_id", specialty_id)
                .add("uid", 15063681).add("time", 1591366330);
        mPresenter.getData(ApiConfig.GET_TRAINING_INFO,map_o);

    }

    @Override
    public void netSuccess(int whichApi, Object[] pD) {
        switch (whichApi){
            case ApiConfig.GET_TRAINING_INFO:
                TrainningBean trainningBean= (TrainningBean) pD[0];
                    lists.addAll(trainningBean.getResult().getLists());
                    premiumAdapter.notifyDataSetChanged();
                break;
        }

    }
}
