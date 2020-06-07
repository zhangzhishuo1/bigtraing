package com.example.bigtraing.view.fragment;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.bigtraing.R;
import com.example.bigtraing.adapter.MyVp_Adapter;
import com.example.bigtraing.base.BaseMvpFragment;
import com.example.bigtraing.course_view.PremiumFragment;
import com.example.bigtraing.model.CouresModel;
import com.example.frame.ICommonModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import butterknife.BindView;

public class CourseFragment extends BaseMvpFragment {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Override
    public void setUpData() {

    }

    @Override
    public void setUpView() {
        ArrayList<Fragment> list = new ArrayList<>();
        list.add(new PremiumFragment(3));
        list.add(new PremiumFragment(1));
        list.add(new PremiumFragment(2));
        MyVp_Adapter myVp_adapter = new MyVp_Adapter(getActivity().getSupportFragmentManager(), list);
       viewPager.setAdapter(myVp_adapter);
       tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("训练营");
        tabLayout.getTabAt(1).setText("精品课");
        tabLayout.getTabAt(2).setText("公开课");


    }

    @Override
    public ICommonModel setModel() {
        return new CouresModel();
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_course;
    }


    @Override
    public void netSuccess(int whichApi, Object[] pD) {

    }
}
