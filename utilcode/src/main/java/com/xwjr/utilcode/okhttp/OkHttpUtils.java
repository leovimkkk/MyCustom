package com.xwjr.utilcode.okhttp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.UserManager;
import android.text.TextUtils;


import com.xwjr.utilcode.okhttp.builder.GetBuilder;
import com.xwjr.utilcode.okhttp.builder.PostFileBuilder;
import com.xwjr.utilcode.okhttp.builder.PostFormBuilder;
import com.xwjr.utilcode.okhttp.builder.PostStringBuilder;
import com.xwjr.utilcode.okhttp.callback.Callback;
import com.xwjr.utilcode.okhttp.cookie.CookieManger;
import com.xwjr.utilcode.okhttp.cookie.SimpleCookieJar;
import com.xwjr.utilcode.okhttp.https.HttpsUtils;
import com.xwjr.utilcode.okhttp.request.RequestCall;
import com.xwjr.utilcode.okhttp.utils.FailureToConnectException;
import com.xwjr.utilcode.utils.LogUtils;
import com.xwjr.utilcode.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;


public class OkHttpUtils {
    public static final String TAG = "OkHttpUtils";
    public static final long DEFAULT_MILLISECONDS = 30000;
    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    public static final String LoginOutReceiverTag = "cn.xwjrfw.p2p.LoginOutReceiver";


    private OkHttpUtils() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        //cookie enabled
        okHttpClientBuilder.cookieJar(new CookieManger(Utils.getContext()));
        mDelivery = new Handler(Looper.getMainLooper());


        if (true) {
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }

        mOkHttpClient = okHttpClientBuilder.build();

    }

    private boolean debug;
    private String tag;

    public OkHttpUtils debug(String tag) {
        debug = true;
        this.tag = tag;
        return this;
    }


    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }


    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }


    public void execute(final RequestCall requestCall, Callback callback) {
        if (debug) {
            if (TextUtils.isEmpty(tag)) {
                tag = TAG;
            }
//           LogUtils.d(tag, "{method:" + requestCall.getRequest().method() + ", detail:" + requestCall.getRequest().toString() + "}");
        }
//        LogUtils.d(tag, "{method:" + requestCall.getRequest().method() + ", detail:" + requestCall.getRequest().toString() + "}");

        LogUtils.i(OkHttpUtils.class.getSimpleName(), "{method:" + requestCall.getRequest().method() + ", detail:" + requestCall.getRequest().toString() + "}");
//        LogUtils.i(OkHttpUtils.class.getSimpleName(), "{method:" + requestCall.toString());

        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {


                LogUtils.i(OkHttpUtils.class.getSimpleName(), "onFailure" + e.toString());
                if (e.toString().contains("Failed to connect")) {
                    try {
                        new FailureToConnectException();
                    } catch (Exception ex) {
                        sendFailResultCallback(call, ex, finalCallback);
                        LogUtils.i(OkHttpUtils.class.getSimpleName(), "onFailure" + ex.toString());
                    }
                    finalCallback.onAfter();
                }
                if (e.toString().contains("Canceled")) {
                    // 取消请求  RxEventBus.getInstance().post();
                    LogUtils.i(OkHttpUtils.class.getSimpleName(), "onFailure" + e.toString());
                    finalCallback.onAfter();
                } else if (e.toString().contains("SocketTimeoutException")) {
                    // 网络超时 在 BaseActivity 中处理
//                    RxEventBus.getInstance().post("提示：网络连接超时");
                    try {
                        new MyTimeOutException();
                    } catch (Exception ex) {
                        sendFailResultCallback(call, ex, finalCallback);
                        LogUtils.i(OkHttpUtils.class.getSimpleName(), "onFailure" + ex.toString());
                    }
                    finalCallback.onAfter();
                } else if (e.toString().contains("SocketException")) {
                    // 网络超时 在 BaseActivity 中处理
//                    RxEventBus.getInstance().post("提示：网络连接异常");
                    finalCallback.onAfter();
                } else if (e.toString().contains("ConnectException")) {
                    // 网络超时 在 BaseActivity 中处理
//                    RxEventBus.getInstance().post("提示：网络连接异常");
                    finalCallback.onAfter();
                } else {
                    //取消请求时不发送异常
                    sendFailResultCallback(call, e, finalCallback);
                }

            }

            @Override
            public void onResponse(final Call call, final Response response) {

                LogUtils.i(OkHttpUtils.class.getSimpleName(), "onResponse" + response.code());
                LogUtils.i(OkHttpUtils.class.getSimpleName(), "onResponse" + response.toString());

                try {
                    if (response.code() == 403) {
                        Intent intent = new Intent();
                        intent.setAction(LoginOutReceiverTag);
                        Utils.getContext().sendBroadcast(intent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


                if (response.code() > 400 && response.code() <= 599) {
                    try {
                        sendFailResultCallback(call, new RuntimeException(response.body().string()), finalCallback);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                } else {

                    try {
                        Object o = finalCallback.parseNetworkResponse(response);
                        sendSuccessResultCallback(o, finalCallback);
                    } catch (Exception e) {
                        sendFailResultCallback(call, e, finalCallback);
                    }
                }


            }
        });
    }


    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback) {
        if (callback == null) return;

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e);
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback) {
        if (callback == null) return;
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                LogUtils.i(OkHttpUtils.class.getSimpleName(), object.toString());
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }


    public void setCertificates(InputStream... certificates) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .sslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, null, null))
                .build();
    }


    public void setConnectTimeout(int timeout, TimeUnit units) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .connectTimeout(timeout, units)
                .build();
    }
}

