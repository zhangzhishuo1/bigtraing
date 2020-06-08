package com.example.bigtraing.view.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.bigtraing.R;
import com.example.bigtraing.adapter.MyVp_Adapter;
import com.example.bigtraing.base.BaseMvpFragment;
import com.example.bigtraing.datafragment.InformationFragment;
import com.example.bigtraing.datafragment.newbestFragment;
import com.example.bigtraing.model.DataModel;
import com.example.frame.ICommonModel;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataFragment extends BaseMvpFragment {

    @BindView(R.id.tv_zl)
    TextView tvZl;
    @BindView(R.id.tv_new)
    TextView tvNew;
    @BindView(R.id.view_pager)
    ViewPager viewPager;


    @Override
    public void setUpData() {

    }

    @Override
    public void setUpView() {
        ArrayList<Fragment> list = new ArrayList<>();
            list.add(new InformationFragment());
            list.add(new newbestFragment());
        MyVp_Adapter myVp_adapter = new MyVp_Adapter(getFragmentManager(), list);
        viewPager.setAdapter(myVp_adapter);
        tvZl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });
        tvNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    tvZl.setEnabled(false);
                    tvNew.setEnabled(true);
                    tvZl.setTextColor(Color.BLACK);
                    tvNew.setTextColor(Color.GRAY);
                } else {
                    tvZl.setEnabled(true);
                    tvNew.setEnabled(false);
                    tvNew.setTextColor(Color.BLACK);
                    tvZl.setTextColor(Color.GRAY);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public ICommonModel setModel() {
        return new DataModel();
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_data;
    }

    @Override
    public void netSuccess(int whichApi, Object[] pD) {

    }
}
