package com.example.bigtraing.model;

import android.content.Context;
import android.text.TextUtils;

import com.example.bigtraing.base.Application1907;
import com.example.bigtraing.constant.Method;
import com.example.frame.ApiConfig;
import com.example.frame.Host;
import com.example.frame.ICommonModel;
import com.example.frame.ICommonPresenter;
import com.example.frame.NetManger;
import com.example.frame.utils.ParamHashMap;

/**
 * Created by 任小龙 on 2020/6/2.
 */
public class LauchModel implements ICommonModel {
    private NetManger mManger = NetManger.getInstance();
    private Context mContext = Application1907.get07ApplicationContext();

    @Override
    public void getData(ICommonPresenter pPresenter, int whichApi, Object[] params) {
        switch (whichApi) {
            case ApiConfig.ADVERT:
                ParamHashMap map = new ParamHashMap().add("w", params[1]).add("h", params[2]).add("positions_id", "APP_QD_01").add("is_show", 0);
                if (!TextUtils.isEmpty((String) params[0])) map.add("specialty_id", params[0]);
                mManger.netWork(NetManger.mService.getAdvert(Host.AD_OPENAPI+ Method.ADVERT_PATH,map), pPresenter, whichApi);
                break;
            case ApiConfig.SUBJECT:
                mManger.netWork(NetManger.mService.getSubjectList(Host.EDU_OPENAPI+ Method.GETALLSPECIALTY), pPresenter, whichApi);
                break;
        }
    }


}
