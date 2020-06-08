package com.example.bigtraing.view.fragment;

import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bigtraing.R;
import com.example.bigtraing.adapter.VipAdapter;
import com.example.bigtraing.base.BaseMvpFragment;
import com.example.bigtraing.model.VipModel;
import com.example.data.SpecialtyChooseEntity;
import com.example.data.VipBzb_Bean;
import com.example.data.VipRecBean;
import com.example.frame.ApiConfig;
import com.example.frame.ICommonModel;
import com.example.frame.constants.ConstantKey;
import com.example.frame.utils.ParamHashMap;
import com.example.utils.newAdd.SharedPrefrenceUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class VIPFragment extends BaseMvpFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private ArrayList<VipBzb_Bean.ResultBean.LiveBeanX.LiveBean> bannliveBeans;
    private List<VipRecBean.ResultBean.ListBean> list;
    private VipAdapter vipAdapter;
    private String specialty_id;
    private int page=1;
    private ParamHashMap map_vip;


    @Override
    public void setUpData() {
        map_vip = new ParamHashMap().add("specialty_id", specialty_id).add("page", page).add("sort", 2);
        mPresenter.getData(ApiConfig.GET_Banner_VIP_INFO,map_vip);
    }

    @Override
    public void setUpView() {
        if (SharedPrefrenceUtils.getObject(getActivity(), ConstantKey.Constan)!=null){
            SpecialtyChooseEntity.DataBean bean=SharedPrefrenceUtils.getObject(getActivity(), ConstantKey.Constan);
            specialty_id = bean.getSpecialty_id();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        bannliveBeans = new ArrayList<>();
        list = new ArrayList<>();
        vipAdapter = new VipAdapter(getActivity(),bannliveBeans,list);
        recyclerView.setAdapter(vipAdapter);
    }

    @Override
    public ICommonModel setModel() {
        return new VipModel();
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_v_i_p;
    }

    @Override
    public void netSuccess(int whichApi, Object[] pD) {
        switch (whichApi){
            case ApiConfig.GET_Banner_VIP_INFO:
                break;
            case ApiConfig.GET_Rec_VIP_INFO:
                break;
        }
    }
}
