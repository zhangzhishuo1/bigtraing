package com.example.bigtraing.avtivity;


import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.example.bigtraing.R;
import com.example.bigtraing.base.BaseSplashActivity;
import com.example.bigtraing.model.LauchModel;
import com.example.frame.ApiConfig;
import com.example.frame.ICommonModel;
import com.example.frame.bean.BaseInfo;
import com.example.frame.bean.LoginInfo;
import com.example.frame.bean.MainAdEntity;
import com.example.frame.bean.SpecialtyChooseEntity;
import com.example.frame.constants.ConstantKey;
import com.example.frame.secret.SystemUtils;
import com.example.utils.newAdd.GlideUtil;
import com.example.utils.newAdd.SharedPrefrenceUtils;

import java.util.concurrent.TimeUnit;

import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.bigtraing.constant.JumpConstant.JUMP_KEY;
import static com.example.bigtraing.constant.JumpConstant.SPLASH_TO_LOGIN;
import static com.example.bigtraing.constant.JumpConstant.SPLASH_TO_SUB;

public class SplashActivity extends BaseSplashActivity {

    private BaseInfo<MainAdEntity> mInfo;
    private Disposable mSubscribe;
    private SpecialtyChooseEntity.DataBean mSelectedInfo;

    @Override
    public ICommonModel setModel() {
        return new LauchModel();
    }

    @Override
    public void setUpView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initDevice();
    }

    @Override
    public void setUpData() {
        mSelectedInfo = SharedPrefrenceUtils.getObject(this, ConstantKey.SUBJECT_SELECT);
        String specialtyId = "";
        if (mSelectedInfo != null && !TextUtils.isEmpty(mSelectedInfo.getSpecialty_id())) {
            mApplication.setSelectedInfo(mSelectedInfo);
            specialtyId = mSelectedInfo.getSpecialty_id();
        }
        Point realSize = SystemUtils.getRealSize(this);
        mPresenter.getData(ApiConfig.ADVERT, specialtyId, realSize.x, realSize.y);
        new Handler().postDelayed(()->{ if (mInfo == null)jump(); },3000);
        LoginInfo loginInfo = SharedPrefrenceUtils.getObject(this,ConstantKey.LOGIN_INFO);
        if (loginInfo != null && !TextUtils.isEmpty(loginInfo.getUid()))mApplication.setLoginInfo(loginInfo);
    }

    @Override
    public void netSuccess(int whichApi, Object[] pD) {
        mInfo = (BaseInfo<MainAdEntity>) pD[0];
        GlideUtil.loadImage(advertImage, mInfo.result.getInfo_url());
        timeView.setVisibility(View.VISIBLE);
        goTime();
    }



    private int preTime = 4;

    private void goTime() {
        mSubscribe = Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(pLong -> {
                    if (preTime - pLong > 0) timeView.setText(preTime - pLong + "s");
                    else jump();
                });
    }

    private void jump() {
//        if (mSubscribe!=null&&!mSubscribe.isDisposed()){
//            mSubscribe.dispose();
//        }
        if (mSubscribe != null)mSubscribe.dispose();
        Observable.just("我是防抖动").debounce(20, TimeUnit.MILLISECONDS).subscribe(p->{
            if (mSelectedInfo != null && !TextUtils.isEmpty(mSelectedInfo.getSpecialty_id())) {
                if (mApplication.isLogin()) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class).putExtra(JUMP_KEY, SPLASH_TO_LOGIN));
                }
            } else {
                startActivity(new Intent(SplashActivity.this, SubjectActivity.class).putExtra(JUMP_KEY, SPLASH_TO_SUB));
            }
            finish();
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscribe != null && !mSubscribe.isDisposed()) mSubscribe.dispose();
    }

    @OnClick({R.id.advert_image, R.id.time_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.advert_image:
                if (mInfo != null) {
//                    mInfo.result.getJump_url();
                }
                break;
            case R.id.time_view:
                jump();
                break;
        }
    }
}
