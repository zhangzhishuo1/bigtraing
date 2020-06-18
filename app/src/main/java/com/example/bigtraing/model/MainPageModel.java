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

public class MainPageModel implements ICommonModel {
    private NetManger manager = NetManger.getInstance();

    @Override
    public void getData(ICommonPresenter pPresenter, int whichApi, Object[] params) {
        switch (whichApi) {
            case ApiConfig.MAIN_PAGE_LIST:
                ParamHashMap add = new ParamHashMap().add("specialty_id", FrameApplication.getFrameApplication().getSelectedInfo().getSpecialty_id()).add("page", params[1]).add("limit", Constants.LIMIT_NUM).add("new_banner", 1);
                manager.netWork(NetManger.mService.getCommonList(Host.EDU_OPENAPI+ Method.GETINDEXCOMMEND,add),pPresenter,whichApi,params[0]);
                break;
            case ApiConfig.BANNER_LIVE:
                ParamHashMap live = new ParamHashMap().add("pro", FrameApplication.getFrameApplication().getSelectedInfo().getSpecialty_id()).add("more_live","1").add("is_new",1).add("new_banner",1);
                manager.netWork(NetManger.mService.getBannerLive(Host.EDU_OPENAPI+Method.GETCAROUSELPHOTO,live),pPresenter,whichApi,params[0]);
                break;
        }
    }


}
