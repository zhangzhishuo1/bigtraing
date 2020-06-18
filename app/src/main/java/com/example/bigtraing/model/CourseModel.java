package com.example.bigtraing.model;


import com.example.bigtraing.constant.Method;
import com.example.frame.ApiConfig;
import com.example.frame.FrameApplication;
import com.example.frame.Host;
import com.example.frame.ICommonModel;
import com.example.frame.ICommonPresenter;
import com.example.frame.NetManger;
import com.example.frame.constants.Constants;
import com.example.frame.utils.ParamHashMap;

import java.util.Map;


public class CourseModel implements ICommonModel {

    private String subjectId = FrameApplication.getFrameApplication().getSelectedInfo().getSpecialty_id();
    @Override
    public void getData(ICommonPresenter pPresenter, int whichApi, Object[] params) {
        switch (whichApi){
            case ApiConfig.COURSE_CHILD:
                ParamHashMap add = new ParamHashMap().add("specialty_id", subjectId).add("page", params[2]).add("limit", Constants.LIMIT_NUM).add("course_type", params[1]);
                NetManger.getInstance().netWork(NetManger.mService.getCourseChildData(Host.EDU_OPENAPI+ Method.GETLESSONLISTFORAPI,add),pPresenter,whichApi,params[0]);
                break;
                //vip
            //vip banner数据
            case ApiConfig.VIP_BANNER_DATA_INFO:
                NetManger.getInstance().netWork(NetManger.mService.getVIPBannerData(Host.EDU_OPENAPI+ Method.VIPBANNER), pPresenter,whichApi,params[0]);
                break;
            //vip 下方recyclerview数据
            case ApiConfig.VIP_BOTTOM_DATA_INFO:
                Map<String, Object> VipMap = (Map<String, Object>) params[1];
                NetManger.getInstance().netWork(NetManger.mService.getVIPBottomData(Host.EDU_OPENAPI+ Method.VIPREC,VipMap), pPresenter,whichApi,params[0]);
                break;
        }
    }


}
