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
import com.example.frame.ApiConfig;
import com.example.frame.LoadTypeConfig;
import com.example.frame.bean.NewbestBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecentlyBestFragment extends BaseMvpFragment<DataModel> {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private NewBestAdapter newBestAdapter;
    private List<NewbestBean.ResultBean> list;
    public int page=1;

    public static RecentlyBestFragment newInstance() {
        RecentlyBestFragment fragment = new RecentlyBestFragment();
        return fragment;
    }


    @Override
    public void setData() {
    mPresenter.getData(ApiConfig.NEWBEAT_DATA_INFO,page, LoadTypeConfig.NORMAL);
    }

    @Override
    public void setView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        list = new ArrayList<>();
        newBestAdapter = new NewBestAdapter(getActivity(),list);
        recyclerView.setAdapter(newBestAdapter);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                refreshLayout.finishLoadMore();
                mPresenter.getData(ApiConfig.NEWBEAT_DATA_INFO,page, LoadTypeConfig.MORE);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                refreshLayout.finishRefresh();
                mPresenter.getData(ApiConfig.NEWBEAT_DATA_INFO,page, LoadTypeConfig.REFRESH);
            }
        });

    }

    @Override
    public DataModel setModel() {
        return new DataModel();
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_recently_best;
    }


    @Override
    public void netSuccess(int whichApi, Object[] pD) {
        switch (whichApi){
           case  ApiConfig.NEWBEAT_DATA_INFO:
            NewbestBean newbestBean= (NewbestBean) pD[0];
            list.addAll(newbestBean.getResult());
            newBestAdapter.notifyDataSetChanged();
        }
    }
}
