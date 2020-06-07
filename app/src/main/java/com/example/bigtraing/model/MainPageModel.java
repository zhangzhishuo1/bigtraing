package com.example.bigtraing.model;


import com.example.frame.ICommonModel;
import com.example.frame.ICommonPresenter;
import com.example.frame.NetManger;

import java.util.HashMap;

public class MainPageModel implements ICommonModel {
    private NetManger manager = NetManger.getInstance();

    @Override
    public void getData(ICommonPresenter pPresenter, int whichApi, Object[] params) {
        manager.netWork(manager.getService("https://baidu/com/").getHeaderInfo(new HashMap<>()), pPresenter, whichApi);
        manager.netWork(manager.getService("https://baidu/com/com/").getHeaderInfo(new HashMap<>()), pPresenter, whichApi);
    }
}
