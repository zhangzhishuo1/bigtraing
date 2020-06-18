package com.example.bigtraing.fragment;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.bigtraing.R;
import com.example.bigtraing.adapter.MyFragmentAdapter;
import com.example.bigtraing.base.BaseMvpFragment;
import com.example.bigtraing.model.CourseModel;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class CourseFragment extends BaseMvpFragment<CourseModel> {


    @BindView(R.id.slide_layout)
    SlidingTabLayout slideLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private List<String> titleList = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();
    private MyFragmentAdapter mFragmentAdapter;

    public static final int TRAINTAB = 3;
    public static final int BESTTAB = 1;
    public static final int PUBLICTAB = 2;

    @Override
    public void setData() {
        Collections.addAll(titleList, "训练营", "精品课", "公开课");
        Collections.addAll(mFragments, CourseChildFragment.getInstance(TRAINTAB), CourseChildFragment.getInstance(BESTTAB), CourseChildFragment.getInstance(PUBLICTAB));
        mFragmentAdapter.notifyDataSetChanged();
        slideLayout.notifyDataSetChanged();
    }

    @Override
    public void setView() {
        mFragmentAdapter = new MyFragmentAdapter(getChildFragmentManager(), mFragments, titleList);
        viewPager.setAdapter(mFragmentAdapter);
        slideLayout.setViewPager(viewPager);
    }

    @Override
    public CourseModel setModel() {
        return null;
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_course;
    }


    @Override
    public void netSuccess(int whichApi, Object[] pD) {

    }
}
