package com.example.bigtraing.avtivity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.example.bigtraing.R;
import com.example.bigtraing.base.BaseMvpActivity;
import com.example.bigtraing.design.LoginView;
import com.example.bigtraing.interfaces.ThirdLoginData;
import com.example.bigtraing.model.AccountModel;
import com.example.frame.ApiConfig;
import com.example.frame.bean.BaseInfo;
import com.example.frame.bean.LoginInfo;
import com.example.frame.bean.PersonHeader;
import com.example.frame.constants.ConstantKey;
import com.example.utils.newAdd.SharedPrefrenceUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhulong.eduvideo.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.bigtraing.constant.JumpConstant.JUMP_KEY;
import static com.example.bigtraing.constant.JumpConstant.REGISTER_TO_LOGIN;
import static com.example.bigtraing.constant.JumpConstant.SPLASH_TO_LOGIN;
import static com.example.bigtraing.constant.JumpConstant.SUB_TO_LOGIN;


public class LoginActivity extends BaseMvpActivity<AccountModel> implements LoginView.LoginViewCallBack {
    @BindView(R.id.login_view)
    LoginView mLoginView;
    private Disposable mSubscribe;
    private String phoneNum;
    private String mFromType;

    @Override
    public AccountModel setModel() {
        return new AccountModel();
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void setUpView() {
        mFromType = getIntent().getStringExtra(JUMP_KEY);
        mLoginView.setLoginViewCallBack(this);
    }

    @Override
    public void setUpData() {

    }

    @Override
    public void netSuccess(int whichApi, Object[] pD) {
        switch (whichApi) {
            case ApiConfig.SEND_VERIFY:
                BaseInfo<String> info = (BaseInfo<String>) pD[0];
                if (info.isSuccess()) {
                    showToast(info.result);
                    goTime();
                } else showToast("验证码发送太频繁，请稍后重试");
                break;
            case ApiConfig.VERIFY_LOGIN:
            case ApiConfig.ACCOUNT_LOGIN:
            case ApiConfig.POST_WE_CHAT_LOGIN_INFO:
                BaseInfo<LoginInfo> baseInfo = (BaseInfo<LoginInfo>) pD[0];
                LoginInfo loginInfo = baseInfo.result;
                if (!TextUtils.isEmpty(phoneNum)) loginInfo.login_name = phoneNum;
                mApplication.setLoginInfo(loginInfo);
                mPresenter.getData(ApiConfig.GET_HEADER_INFO);
                break;
            case ApiConfig.GET_HEADER_INFO:
                doPre();
                PersonHeader personHeader = ((BaseInfo<PersonHeader>) pD[0]).result;
                mApplication.getLoginInfo().personHeader = personHeader;
                SharedPrefrenceUtils.putObject(this, ConstantKey.LOGIN_INFO, mApplication.getLoginInfo());
                jump();
                break;
            case ApiConfig.GET_WE_CHAT_TOKEN:
                JSONObject allJson = null;
                try {
                    allJson = new JSONObject(pD[0].toString());
                } catch (JSONException pE) {
                    pE.printStackTrace();
                }
                ThirdLoginData thirdData = new ThirdLoginData(3);
                thirdData.setOpenid(allJson.optString("openid"));
                thirdData.token = allJson.optString("access_token");
                thirdData.refreshToken = allJson.optString("refresh_token");
                thirdData.utime = allJson.optLong("expires_in") * 1000;
                thirdData.unionid = allJson.optString("unionid");
                mPresenter.getData(ApiConfig.POST_WE_CHAT_LOGIN_INFO, thirdData);
                break;
        }
    }
////最终永远登录得都是微信平台  而不是账号
    private void jump() {
        if (mFromType.equals(SPLASH_TO_LOGIN) || mFromType.equals(SUB_TO_LOGIN))
            startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private long time = 60l;

    private void goTime() {
        mSubscribe = Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(goTime -> {
            mLoginView.getVerifyCode.setText(time - goTime + "s");
            if (time - goTime < 1) {
                doPre();//倒计时到0秒停止倒计时
                mLoginView.getVerifyCode.setText("获取验证码");
            }
        });
    }

    private void doPre() {
        if (mSubscribe != null && !mSubscribe.isDisposed()) mSubscribe.dispose();
    }

    @OnClick({R.id.close_login, R.id.register_press, R.id.forgot_pwd, R.id.login_by_qq, R.id.login_by_wx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close_login:
                if (!TextUtils.isEmpty(mFromType) && (mFromType.equals(SUB_TO_LOGIN) || mFromType.equals(SPLASH_TO_LOGIN) || mFromType.equals(REGISTER_TO_LOGIN))) {
                    startActivity(new Intent(this, HomeActivity.class));
                }
                finish();
                break;
            case R.id.register_press:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.forgot_pwd:
                break;
            case R.id.login_by_qq:
                break;
            case R.id.login_by_wx:
                doWechatLogin();
                break;
        }
    }

    private void doWechatLogin() {
        WXEntryActivity.setOnWeChatLoginResultListener(it -> {
            int errorCode = it.getIntExtra("errorCode", 0);
            //微信给得tok值
            String normalCode = it.getStringExtra("normalCode");
            switch (errorCode) {
                case 0:
                    showLog("用户已同意微信登录");
                    mPresenter.getData(ApiConfig.GET_WE_CHAT_TOKEN, normalCode);
                    break;
                case -4:
                    showToast("用户拒绝授权");
                    break;
                case -2:
                    showToast("用户取消");
                    break;

            }
        });
        IWXAPI weChatApi = WXAPIFactory.createWXAPI(this, null);
        weChatApi.registerApp(ConstantKey.WX_APP_ID);
        if (weChatApi.isWXAppInstalled()) {
            doWeChatLogin();
        } else showToast("请先安装微信");
    }

    private void doWeChatLogin() {
        SendAuth.Req request = new SendAuth.Req();
//        snsapi_base 和snsapi_userinfo  静态获取和同意后获取
        request.scope = "snsapi_userinfo";
        request.state = "com.zhulong.eduvideo";
        IWXAPI weChatApi = WXAPIFactory.createWXAPI(this, ConstantKey.WX_APP_ID);
        weChatApi.sendReq(request);
    }

    @Override
    public void sendVerifyCode(String phoneNum) {
        this.phoneNum = phoneNum;
        mPresenter.getData(ApiConfig.SEND_VERIFY, phoneNum);
    }

    @Override
    public void loginPress(int type, String userName, String pwd) {
        if (mLoginView.mCurrentLoginType == mLoginView.VERIFY_TYPE)
            mPresenter.getData(ApiConfig.VERIFY_LOGIN, userName, pwd);
        else mPresenter.getData(ApiConfig.ACCOUNT_LOGIN, userName, pwd);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doPre();
        //页面销毁后停止倒计时，防止观察者仍处于订阅状态，引发因持有引用问题影响对象回收
    }
}

