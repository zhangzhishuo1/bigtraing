package com.example.bigtraing.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bigtraing.R;
import com.example.bigtraing.adapter.CourseChildAdapter;
import com.example.bigtraing.base.BaseMvpFragment;
import com.example.bigtraing.interfaces.DataListener;
import com.example.bigtraing.model.CourseModel;
import com.example.frame.ApiConfig;
import com.example.frame.LoadTypeConfig;
import com.example.frame.bean.BaseInfo;
import com.example.frame.bean.CourseListInfo;
import com.example.frame.bean.SearchItemEntity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class CourseChildFragment extends BaseMvpFragment<CourseModel> implements DataListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int mIndex;
    private int page = 1;
    private  List<SearchItemEntity> mList = new ArrayList<>();
    private CourseChildAdapter mAdapter;

    public static CourseChildFragment getInstance(int index) {
        CourseChildFragment fragment = new CourseChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("whichFragment", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIndex = (int) getArguments().get("whichFragment");
        }
    }

    @Override
    public void setData() {
        mPresenter.getData(ApiConfig.COURSE_CHILD, LoadTypeConfig.NORMAL, mIndex, page);
    }

    @Override
    public void setView() {
        initRecyclerView(recyclerView, refreshLayout, this);
        mAdapter = new CourseChildAdapter(mList, getContext());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public CourseModel setModel() {
        return new CourseModel();
    }

    @Override
    public int setLayoutId() {
        return R.layout.refresh_list_layout;
    }



    @Override
    public void netSuccess(int whichApi, Object[] pD) {
        BaseInfo<CourseListInfo> baseInfo = (BaseInfo<CourseListInfo>) pD[0];
        if (baseInfo.isSuccess()){
            List<SearchItemEntity> lists = baseInfo.result.lists;
            int load = (int) pD[1];
            if (load == LoadTypeConfig.REFRESH){
                refreshLayout.finishRefresh();
                mList.clear();
            } else if (load == LoadTypeConfig.MORE)refreshLayout.finishLoadMore();
            mList.addAll(lists);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void dataType(int mode) {
        if (mode == LoadTypeConfig.REFRESH)
            mPresenter.getData(ApiConfig.COURSE_CHILD, LoadTypeConfig.REFRESH, mIndex, 1);
        else {
            page++;
            mPresenter.getData(ApiConfig.COURSE_CHILD, LoadTypeConfig.MORE, mIndex, page);
        }
    }
}
