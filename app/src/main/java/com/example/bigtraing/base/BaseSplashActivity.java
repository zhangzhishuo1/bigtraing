package com.example.bigtraing.base;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.bigtraing.R;
import com.example.frame.FrameApplication;
import com.example.frame.bean.Device;
import com.example.frame.secret.SystemUtils;
import com.example.utils.NetworkUtils;

import butterknife.BindView;

public abstract class BaseSplashActivity extends BaseMvpActivity {
    @BindView(R.id.advert_image)
    public ImageView advertImage;
    @BindView(R.id.time_view)
    public TextView timeView;

    @Override
    public int setLayoutId() {
        return R.layout.activity_splash;
    }

    public void initDevice() {
        Device device = new Device();
        device.setScreenWidth(SystemUtils.getSize(this).x);
        device.setScreenHeight(SystemUtils.getSize(this).y);
        device.setDeviceName(SystemUtils.getDeviceName());
        device.setSystem(SystemUtils.getSystem(this));
        device.setVersion(SystemUtils.getVersion(this));
        device.setDeviceId(SystemUtils.getDeviceId(this));
        device.setLocalIp(NetworkUtils.getLocalIpAddress());
        FrameApplication.getFrameApplication().setDeviceInfo(device);
    }
}
