package com.example.bigtraing.base;

import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bigtraing.interfaces.DataListener;
import com.example.frame.LoadTypeConfig;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;


public class BaseFragment extends Fragment {

    public void initRecyclerView(RecyclerView pRecyclerView, SmartRefreshLayout pRefreshLayout, DataListener pDataListener) {
        if (pRecyclerView != null) pRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (pRefreshLayout != null && pDataListener != null) {
            pRefreshLayout.setOnRefreshListener(refreshLayout -> pDataListener.dataType(LoadTypeConfig.REFRESH));
            pRefreshLayout.setOnLoadMoreListener(refreshLayout -> pDataListener.dataType(LoadTypeConfig.MORE));
        }
    }

    public void showLog(Object content) {
        Log.e("睚眦", content.toString());
    }

    public void showToast(Object content) {
        Toast.makeText(getContext(), content.toString(), Toast.LENGTH_SHORT).show();
    }
}
