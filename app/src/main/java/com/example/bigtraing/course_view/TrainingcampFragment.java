package com.example.bigtraing.course_view;

import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bigtraing.R;
import com.example.bigtraing.adapter.TrainingAdapter;
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

public class TrainingcampFragment extends BaseMvpFragment<CouresModel> {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private int page = 1;
    private String specialty_id;
    private ParamHashMap map;
    private List<TrainningBean.ResultBean.ListsBean> lists;
    private TrainingAdapter trainingAdapter;
    private int course_type;

    public TrainingcampFragment(int course_type) {
        this.course_type = course_type;
    }

    @Override
    public CouresModel setModel() {
        return new CouresModel();
    }

    @Override
    public int setLayoutId() {
        return R.layout.traiing_frag;
    }

    @Override
    public void setUpView() {
        if (SharedPrefrenceUtils.getObject(getActivity(), ConstantKey.Constan) != null) {
            SpecialtyChooseEntity.DataBean bean = SharedPrefrenceUtils.getObject(getActivity(), ConstantKey.Constan);
            specialty_id = bean.getSpecialty_id();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                map = new ParamHashMap().add("page", page).add("course_type", course_type)
                        .add("limit", 15).add("specialty_id", specialty_id)
                        .add("uid", 15063681).add("time", 1591366329);
                mPresenter.getData(ApiConfig.GET_TRAINING_INFO,map);
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                lists.clear();
                map = new ParamHashMap().add("page", page).add("course_type", course_type)
                        .add("limit", 15).add("specialty_id", specialty_id)
                        .add("uid", 15063681).add("time", 1591366329);
                mPresenter.getData(ApiConfig.GET_TRAINING_INFO,map);
                refreshLayout.finishRefresh();
            }
        });
        lists = new ArrayList<>();
        trainingAdapter = new TrainingAdapter(getActivity(),lists);
        recyclerView.setAdapter(trainingAdapter);

    }

    @Override
    public void setUpData() {
        map = new ParamHashMap().add("page", page).add("course_type", course_type)
                .add("limit", 15).add("specialty_id", specialty_id)
                .add("uid", 15063681).add("time", 1591366329);
        mPresenter.getData(ApiConfig.GET_TRAINING_INFO,map);
    }

    @Override
    public void netSuccess(int whichApi, Object[] pD) {
        switch (whichApi){
            case ApiConfig.GET_TRAINING_INFO:
              TrainningBean trainningBean= (TrainningBean) pD[0];
               lists.addAll(trainningBean.getResult().getLists());
                trainingAdapter.notifyDataSetChanged();
                Log.i("1111111",  trainningBean.toString());
                break;
        }

    }
}
