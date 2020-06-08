package com.example.bigtraing.model;

import android.content.Context;

import com.example.bigtraing.base.Application1907;
import com.example.frame.ICommonModel;
import com.example.frame.ICommonPresenter;
import com.example.frame.NetManger;

public class VipModel implements ICommonModel {
    NetManger manger=NetManger.getInstance();
    private Context context= Application1907.get07ApplicationContext();
    @Override
    public void getData(ICommonPresenter pPresenter, int whichApi, Object[] params) {

    }
}
