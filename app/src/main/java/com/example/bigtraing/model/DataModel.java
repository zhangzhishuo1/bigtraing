package com.example.bigtraing.model;


import android.content.Context;


import com.example.bigtraing.base.Application1907;
import com.example.frame.ApiConfig;
import com.example.frame.ICommonModel;
import com.example.frame.ICommonPresenter;
import com.example.frame.NetManger;

import java.util.Map;

public class DataModel implements ICommonModel {
    NetManger manger=NetManger.getInstance();
    private Context context= Application1907.get07ApplicationContext();
    @Override
    public void getData(ICommonPresenter pPresenter, int whichApi, Object[] params) {
        switch (whichApi){
          case   ApiConfig.GET_INFORMATION_INFO:
              Map<String, Object> infom = (Map<String, Object>) params[0];
              manger.netWork(manger.getService(" https://bbs.zhulong.com").getInformation(infom),pPresenter,whichApi);
       break;
            case ApiConfig.GET_NEWBEST_INFO:
                Map<String,Object>newbest=(Map<String,Object>)params[0];
                manger.netWork(manger.getService("https://bbs.zhulong.com/").getnewbest(newbest),pPresenter,whichApi);
                break;
        }
    }
}
