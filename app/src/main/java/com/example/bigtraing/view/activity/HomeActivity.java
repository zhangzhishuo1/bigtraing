package com.example.bigtraing.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.example.bigtraing.R;
import com.example.bigtraing.base.BaseMvpActivity;
import com.example.bigtraing.model.CommonHomeModel;

public class HomeActivity extends BaseMvpActivity<CommonHomeModel> {
    public NavController mProjectController;

    @Override
    public CommonHomeModel setModel() {
        return null;
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void setUpView() {
        mProjectController = Navigation.findNavController(this, R.id.project_fragment_control);
    }

    @Override
    public void setUpData() {

    }

    @Override
    public void netSuccess(int whichApi, Object[] pD) {

    }
}
