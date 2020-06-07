package com.example.bigtraing.view.fragment;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bigtraing.R;
import com.example.bigtraing.adapter.LiveAdapter;
import com.example.bigtraing.adapter.MainPageListAdapter;
import com.example.bigtraing.base.Application1907;
import com.example.bigtraing.base.BaseMvpFragment;
import com.example.bigtraing.interfaces.DataListener;
import com.example.bigtraing.model.MainPageModel;
import com.example.data.BaseInfo;
import com.example.data.LiveBean;
import com.example.data.LiveListBean;
import com.example.data.MainPageBean;
import com.example.data.SpecialtyChooseEntity;
import com.example.frame.ApiConfig;
import com.example.frame.ICommonModel;
import com.example.frame.LoadTypeConfig;
import com.example.frame.utils.ParamHashMap;
import com.example.utils.newAdd.GsonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainPageFragment extends BaseMvpFragment {

    @BindView(R.id.live_group)
    RecyclerView liveGroup;
    @BindView(R.id.zhibo)
    ConstraintLayout zhibo;
    @BindView(R.id.main_page_recycler)
    RecyclerView mainPageRecycler;
    @BindView(R.id.main_page_smart)
    SmartRefreshLayout mainPageSmart;
    @BindView(R.id.main_page_banner)
    Banner mainPageBanner;
    @BindView(R.id.views)
    View views;
    @BindView(R.id.tv_shortcuts_2)
    TextView tvShortcuts2;
    @BindView(R.id.tv_shortcuts_6)
    TextView tvShortcuts6;
    private String specialty_id;
    private LiveAdapter liveAdapter;
    private int page;
    private MainPageListAdapter mainPageListAdapter;


    @Override
    public ICommonModel setModel() {
        return new MainPageModel();
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_main_page;
    }



    @Override
    public void setUpData() {
        SpecialtyChooseEntity.DataBean selectedInfo = Application1907.getFrameApplication().getSelectedInfo();
        specialty_id = selectedInfo.getSpecialty_id();

        ParamHashMap add_m = new ParamHashMap().add("pro", specialty_id).add("more_live", "1").add("is_new", 1).add("new_banner", 1);
        mPresenter.getData(ApiConfig.GET_BANNER_LIVE, add_m);

        ParamHashMap add1 = new ParamHashMap().add("specialty_id", specialty_id).add("page", page).add("limit", 10);
        mPresenter.getData(ApiConfig.GET_List_LIVE, LoadTypeConfig.NORMAL, add1);
        initRecyclerView(mainPageRecycler, mainPageSmart, new DataListener() {
            @Override
            public void dataType(int mode) {
                if(mode==LoadTypeConfig.REFRESH){
                    page=0;
                    ParamHashMap add1 = new ParamHashMap().add("specialty_id", specialty_id).add("page", page).add("limit", 10);
                    mPresenter.getData(ApiConfig.GET_List_LIVE, LoadTypeConfig.REFRESH,add1);
                }else{
                    page++;
                    ParamHashMap add1 = new ParamHashMap().add("specialty_id", specialty_id).add("page", page).add("limit", 10);
                    mPresenter.getData(ApiConfig.GET_List_LIVE, LoadTypeConfig.MORE,add1);
                }
            }
        });
    }

    @Override
    public void setUpView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        liveGroup.setLayoutManager(linearLayoutManager);
        liveAdapter = new LiveAdapter(getActivity());
        liveGroup.setAdapter(liveAdapter);
        mainPageRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainPageListAdapter = new MainPageListAdapter(getActivity());
        mainPageRecycler.setAdapter(mainPageListAdapter);

    }

    @Override
    public void netSuccess(int whichApi, Object[] pD) {
        switch (whichApi) {
            case ApiConfig.GET_BANNER_LIVE:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (whichApi) {
                            case ApiConfig.GET_BANNER_LIVE:
                                String s = (String) pD[0];

                                JSONObject object = null;
                                try {
                                    object = new JSONObject(s).optJSONObject("result");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                JSONArray object_list = object.optJSONArray("Carousel");
                                List<MainPageBean.CarouselBean> list = GsonUtil.jsonToList(object_list.toString(), MainPageBean.CarouselBean.class);
                                if (list != null && list.size() != 0) {
                                    mainPageBanner.setImages(list).setImageLoader(new ImageLoader() {
                                        @Override
                                        public void displayImage(Context context, Object path, ImageView imageView) {
                                            MainPageBean.CarouselBean path1 = (MainPageBean.CarouselBean) path;
                                            Glide.with(context).load(path1.getImg()).into(imageView);
                                        }
                                    }).start();
                                }

                                List<LiveBean> liveingEntitys = GsonUtil.jsonToList(object.optString("live"), LiveBean.class);
                                if (liveingEntitys != null && liveingEntitys.size() != 0) {
                                    Log.i("---------liveingEntitys", "run: "+  liveingEntitys.toString());

                                    liveAdapter.addList(liveingEntitys);
                                } else {
                                    zhibo.setVisibility(View.GONE);
                                    views.setVisibility(View.GONE);
                                }
                                break;
                        }
                    }
                });
                break;
            case ApiConfig.GET_List_LIVE:
                Integer[] integers = (Integer[]) pD[1];
                int loatType = integers[0].intValue();

                BaseInfo<List<LiveListBean>> bean = (BaseInfo<List<LiveListBean>>) pD[0];
                List<LiveListBean> result = bean.result;
                if (loatType == LoadTypeConfig.NORMAL) {
                    mainPageListAdapter.addList(result);
                } else if (loatType == LoadTypeConfig.MORE) {
                    mainPageSmart.finishLoadMore();
                    mainPageListAdapter.addList(result);
                }
                break;
        }
    }
}
