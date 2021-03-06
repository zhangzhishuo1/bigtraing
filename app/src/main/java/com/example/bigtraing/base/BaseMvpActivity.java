package com.example.bigtraing.base;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.example.frame.CommonPresenter;
import com.example.frame.ICommonModel;
import com.example.frame.ICommonView;

import butterknife.ButterKnife;

public abstract class BaseMvpActivity<M extends ICommonModel> extends BaseActivity implements ICommonView {
    private M mModel;
    public CommonPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
        ButterKnife.bind(this);
        mModel = setModel();
        mPresenter = new CommonPresenter(this, mModel);
        setUpView();
        setUpData();
    }

    public abstract M setModel();

    public abstract int setLayoutId();

    public abstract void setUpView();

    public abstract void setUpData();

    public abstract void netSuccess(int whichApi, Object[] pD);

    public void netFailed(int whichApi, Throwable pThrowable) {
    }

    @Override
    public void onSuccess(int whichApi, Object[] pD) {
        netSuccess(whichApi, pD);
    }

    @Override
    public void onFailed(int whichApi, Throwable pThrowable) {
        showLog("net work error: " + whichApi + "error content" + pThrowable != null && !TextUtils.isEmpty(pThrowable.getMessage()) ? pThrowable.getMessage() : "不明错误类型");
        netFailed(whichApi, pThrowable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.clear();
    }
}
