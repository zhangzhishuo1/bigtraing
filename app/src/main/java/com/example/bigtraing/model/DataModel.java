package com.example.bigtraing.model;


import com.example.bigtraing.R;
import com.example.bigtraing.constant.Method;
import com.example.frame.ApiConfig;
import com.example.frame.FrameApplication;
import com.example.frame.Host;
import com.example.frame.ICommonModel;
import com.example.frame.ICommonPresenter;
import com.example.frame.NetManger;
import com.example.frame.utils.ParamHashMap;

import java.util.Map;

public class DataModel implements ICommonModel {
    @Override
    public void getData(ICommonPresenter pPresenter, int whichApi, Object[] params) {
        switch (whichApi) {
            case ApiConfig.DATA_GROUP:
                ParamHashMap add = new ParamHashMap().add("type", 1).add("fid", FrameApplication.getFrameApplication().getSelectedInfo().getFid()).add("page", params[1]);
                NetManger.getInstance().netWork(NetManger.mService.getGroupList(Host.BBS_OPENAPI+ Method.GETGROUPLIST,add),pPresenter,whichApi,params[0]);
                break;
            case ApiConfig.CLICK_CANCEL_FOCUS:
                ParamHashMap add1 = new ParamHashMap().add("group_id", params[0]).add("type", 1).add("screctKey", FrameApplication.getFrameApplicationContext().getString(R.string.secrectKey_posting));
                NetManger.getInstance().netWork(NetManger.mService.removeFocus(Host.BBS_API+Method.REMOVEGROUP,add1),pPresenter,whichApi,params[1]);
                break;
            case ApiConfig.CLICK_TO_FOCUS:
                ParamHashMap add2 = new ParamHashMap().add("gid", params[0]).add("group_name", params[1]).add("screctKey", FrameApplication.getFrameApplicationContext().getString(R.string.secrectKey_posting));
                NetManger.getInstance().netWork(NetManger.mService.focus(Host.BBS_API+Method.JOINGROUP,add2),pPresenter,whichApi,params[2]);
                break;
            //	//资料 最新精华
            //	https://bbs.zhulong.com/openapi/group/getThreadEssence?
            //page=1&fid=29&uid=15063681&time=1591368576&devices=oppoR11&
            //system=android,5.1.1&version=2.1.4&unique_id=355757265852349&client_id=205
            case ApiConfig.NEWBEAT_DATA_INFO:
                ParamHashMap add3 = new ParamHashMap().add("page", params[0]).add("fid", FrameApplication.getFrameApplication().getSelectedInfo().getFid());
                NetManger.getInstance().netWork(NetManger.mService.getNewBestBean(Host.BBS_API+Method.NEWBEST,add3),pPresenter,whichApi,params[1]);
                break;
            case ApiConfig.GROUP_DETAIL:
                NetManger.getInstance().netWork(NetManger.mService.getGroupDetail(Host.BBS_OPENAPI+Method.GETGROUPTHREADLIST,params[0]),pPresenter,whichApi);
                break;
            case ApiConfig.GROUP_DETAIL_FOOTER_DATA:
                NetManger.getInstance().netWork(NetManger.mService.getGroupDetailFooterData(Host.BBS_OPENAPI+Method.GETGROUPTHREADLIST, (Map<String, Object>) params[1]),pPresenter,whichApi,params[0]);
                break;
        }
    }
}

