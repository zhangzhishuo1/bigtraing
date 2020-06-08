package com.example.bigtraing.datafragment;


import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bigtraing.R;
import com.example.bigtraing.adapter.NewBestAdapter;
import com.example.bigtraing.base.BaseMvpFragment;
import com.example.bigtraing.model.DataModel;
import com.example.data.NewbestBean;
import com.example.data.SpecialtyChooseEntity;
import com.example.frame.ApiConfig;
import com.example.frame.ICommonModel;
import com.example.frame.constants.ConstantKey;
import com.example.frame.utils.ParamHashMap;
import com.example.utils.newAdd.SharedPrefrenceUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class newbestFragment extends BaseMvpFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private NewBestAdapter newBestAdapter;
    private List<NewbestBean.ResultBean> list;
    private int page=1;
    private int fid;
    private ParamHashMap map_add;

    @Override
    public void setUpData() {
        map_add = new ParamHashMap().add("page", page).add("fid", fid);
        mPresenter.getData(ApiConfig.GET_NEWBEST_INFO,map_add);

    }

    @Override
    public void setUpView() {
        if ( SharedPrefrenceUtils.getObject(getActivity(), ConstantKey.Constan)!=null){
            SpecialtyChooseEntity.DataBean bean=  SharedPrefrenceUtils.getObject(getActivity(), ConstantKey.Constan);
            fid = bean.getFid();
        }
        SharedPrefrenceUtils.getObject(getActivity(), ConstantKey.Constan);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                map_add = new ParamHashMap().add("page", page).add("fid", fid);
                mPresenter.getData(ApiConfig.GET_NEWBEST_INFO,map_add);

                refreshLayout.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                list.clear();
                map_add = new ParamHashMap().add("page", page).add("fid", fid);
                mPresenter.getData(ApiConfig.GET_NEWBEST_INFO,map_add);
                refreshLayout.finishRefresh();
            }
        });

        list = new ArrayList<>();
        newBestAdapter = new NewBestAdapter(getActivity(), list);
        recyclerView.setAdapter(newBestAdapter);
    }

    @Override
    public ICommonModel setModel() {
        return new DataModel();
    }

    @Override
    public int setLayoutId() {
        return R.layout.newbest_fragment;
    }

    @Override
    public void netSuccess(int whichApi, Object[] pD) {
        switch (whichApi){
            case ApiConfig.GET_NEWBEST_INFO:
                NewbestBean newbestBean=(NewbestBean)pD[0];
//                list.addAll(newbestBean.getResult());
                newBestAdapter.notifyDataSetChanged();
                break;
        }

    }
}
