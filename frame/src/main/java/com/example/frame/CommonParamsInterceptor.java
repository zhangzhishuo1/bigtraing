package com.example.frame;

import android.text.TextUtils;
import android.util.Log;

import com.example.data.Device;
import com.example.data.LoginInfo;
import com.example.frame.constants.ConstantKey;
import com.example.frame.constants.Constants;
import com.example.frame.secret.Md5Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class CommonParamsInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request newRequest = null;
        Request request = chain.request();
        if (request.method().equalsIgnoreCase("GET")) {
            newRequest = addUrl(request);
        }
        if (request.method().equalsIgnoreCase("POST")) {
            RequestBody preBody = request.body();
            if (preBody instanceof FormBody || preBody.contentType()!=null && preBody.contentType().toString().contains("x-www-form-urlencoded")){
                newRequest = addFormPrams(request);
            } else if (preBody.contentType() != null && preBody.contentType().toString().contains("application/json")) {
                newRequest = addJsonStr(request);
            } else if (preBody.contentType() != null && preBody.contentType().toString().contains("multipart/form-data")) {
                if (!(preBody instanceof MultipartBody)) {
                    newRequest = addUrl(request);
                }
            } else {
                newRequest = addFormPrams(request);
            }
        }
        return chain.proceed(newRequest != null ? newRequest : request);
    }

    private Request addJsonStr(Request pRequest) {
        Buffer buffer = new Buffer();
        try {
            pRequest.body().writeTo(buffer);
        } catch (IOException pE) {
            pE.printStackTrace();
        }
        String oldJsonParams = buffer.readUtf8();
        HashMap rootMap = new Gson().fromJson(oldJsonParams, HashMap.class); // 原始参数
        rootMap.put("client_id", Constants.CLIENT_ID);
        String newJsonParams = new Gson().toJson(rootMap);
        return pRequest.newBuilder().post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), newJsonParams)).build();
    }

    private Request addUrl(Request request) {
        Device device = FrameApplication.getFrameApplication().getDeviceInfo();
        LoginInfo info = FrameApplication.getFrameApplication().getLoginInfo();
        String uid = info != null && !TextUtils.isEmpty(info.getUid()) ? info.getUid() : "0";
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        HttpUrl httpUrl = request.url()
                .newBuilder()
                .scheme(request.url().scheme())
                .host(request.url().host())
                .addQueryParameter("uid", uid)
                .addQueryParameter("client_id", Constants.CLIENT_ID)
                .addQueryParameter("time", time)
                .addQueryParameter("system", device.getSystem())
                .addQueryParameter("version", device.getVersion())
                .addQueryParameter("unique_id", device.getDeviceId())
                .build();
        return request.newBuilder()
                .method(request.method(), request.body())
                .url(httpUrl)
                .build();
    }

    private String getToken(String uid, String appid, String time, String functionName, String secrectKey) {
        String token = "";
        StringBuilder sb = new StringBuilder();
        sb.append(appid).append(secrectKey).append(time).append(functionName)
                .append(uid);
        token = Md5Util.getStringMd5(sb.toString());
        return token;
    }

    private String getFunctionUrl(String containsFunctionUrl) {
        String[] split1;
        if (containsFunctionUrl.contains("?")) {
            String[] split = containsFunctionUrl.split("\\?");
            split1 = split[0].split("/");
        } else
            split1 = containsFunctionUrl.split("/");
        return split1[split1.length - 1];
    }

    private Request addFormPrams(Request request) {
        String screctKey = FrameApplication.getFrameApplicationContext().getString(R.string.secrectKey_passport);
        Device device = FrameApplication.getFrameApplication().getDeviceInfo();
        LoginInfo info = FrameApplication.getFrameApplication().getLoginInfo();
        String uid = info != null && !TextUtils.isEmpty(info.getUid()) ? info.getUid() : "0";
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (request.body() instanceof FormBody) {
            FormBody formBody = (FormBody) request.body();
            for (int i = 0; i < formBody.size(); i++) {
                if (formBody.encodedName(i).equals("uid")){
                    uid = formBody.encodedValue(i);
                }
                if(formBody.encodedName(i).equals(ConstantKey.SECRET_KEY)){
                    try {//screctKey作为参数传递过程中进行了utf-8的编码，这里要用秘钥生成token，必须解码
                        screctKey = URLDecoder.decode(formBody.encodedValue(i),"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
            }
        }
        bodyBuilder.addEncoded("appid", Constants.appid);
        bodyBuilder.addEncoded("uid", uid);
        bodyBuilder.addEncoded("time", time);
        bodyBuilder.addEncoded("token", getToken(uid, Constants.appid, time, getFunctionUrl(request.url().toString()), screctKey));
        bodyBuilder.addEncoded("devices", device.getDeviceName());
        bodyBuilder.addEncoded("system", device.getSystem());
        bodyBuilder.addEncoded("version", device.getVersion());
        bodyBuilder.addEncoded("unique_id", device.getDeviceId());
        bodyBuilder.addEncoded("client_id", Constants.CLIENT_ID);
        FormBody newBody = bodyBuilder.build();
        for (int i = 0; i < newBody.size(); i++) {
            Log.d("---post form ", "common params added---" + newBody.name(i) + " " + newBody.value(i));
        }
        return request.newBuilder().post(newBody).build();
    }

    private Request addFormUrl(Request request) {
        String screctKey = FrameApplication.getFrameApplicationContext().getString(R.string.secrectKey_passport);
        Device device = FrameApplication.getFrameApplication().getDeviceInfo();
        LoginInfo info = FrameApplication.getFrameApplication().getLoginInfo();
        String uid = info != null && !TextUtils.isEmpty(info.getUid()) ? info.getUid() : "0";
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        HttpUrl.Builder httpBuilder = request.url().newBuilder()
                .addEncodedQueryParameter("uid", uid)
                .addEncodedQueryParameter("appid", Constants.appid)
                .addEncodedQueryParameter("time", time)
                .addEncodedQueryParameter("token", getToken(uid, Constants.appid, time, getFunctionUrl(request.url().toString()), screctKey))
                .addEncodedQueryParameter("devices", device.getDeviceName())
                .addEncodedQueryParameter("system", device.getSystem())
                .addEncodedQueryParameter("version", device.getVersion())
                .addEncodedQueryParameter("unique_id", device.getDeviceId())
                .addEncodedQueryParameter("client_id", Constants.CLIENT_ID);
        return request.newBuilder().method(request.method(), request.body()).url(httpBuilder.build()).build();
    }

}
