package com.example.bigtraing.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.data.VipBzb_Bean;
import com.example.data.VipRecBean;

import java.util.ArrayList;
import java.util.List;


public class VipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<VipBzb_Bean.ResultBean.LiveBeanX.LiveBean> bannliveBeans;
    private List<VipRecBean.ResultBean.ListBean> list;
    private static final int type1_banner=1;
    private static final int type2_live=2;
    private static final int type3_list=3;

    public VipAdapter(Context context, ArrayList<VipBzb_Bean.ResultBean.LiveBeanX.LiveBean> bannliveBeans, List<VipRecBean.ResultBean.ListBean> list) {
        this.context = context;
        this.bannliveBeans = bannliveBeans;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
