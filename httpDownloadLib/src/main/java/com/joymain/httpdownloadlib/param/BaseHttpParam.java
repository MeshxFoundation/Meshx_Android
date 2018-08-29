package com.joymain.httpdownloadlib.param;

import android.content.Context;

import com.joymain.httpdownloadlib.callback.HttpRequestCallback;
import com.joymain.httpdownloadlib.callback.HttpResultCallback;
import com.joymain.httpdownloadlib.subscriber.HttpSubscriberClient;

import java.lang.ref.SoftReference;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by lzy on 2017-4-27.
 * 网络请求参数
 */
public abstract class BaseHttpParam {

    public final static int PAGE_SIZE = 10;

    //rx生命周期管理
    protected SoftReference<Context> rxAppCompatActivity;
    /*成功回调*/
    protected SoftReference<HttpResultCallback> resultCallback;
    /*请求回调*/
    protected HttpRequestCallback requestCallback;
    /*是否能取消加载框*/
    protected boolean cancel = true;
    /*是否显示加载框*/
    protected boolean showProgress = true;
    /*基础url*/
    protected String baseUrl;
    /*连接时间*/
    protected long connectionTime = 10;
    /*是否需要公共参数*/
    protected boolean needCommonReqParam = true;
    /*请求码，用于返回区分接口*/
    protected int requestCode = -1;
    /*是否自动添加Token*/
    protected boolean needToken = true;
    /*分页*/
    protected int page = 0;

    protected Object extraInfo;

    //请求参数无需公共参数的构造器
    public BaseHttpParam(Context activity, HttpResultCallback resultCallback,
                         HttpRequestCallback requestCallback) {
        this.resultCallback = new SoftReference<>(resultCallback);
        this.requestCallback = requestCallback;
        this.rxAppCompatActivity = new SoftReference<>(activity);
        this.needCommonReqParam = false;
    }

    public BaseHttpParam(Context activity, HttpResultCallback resultCallback,
                         int requestCode, HttpRequestCallback requestCallback) {
        this.resultCallback = new SoftReference<>(resultCallback);
        this.requestCallback = requestCallback;
        this.rxAppCompatActivity = new SoftReference<>(activity);
        this.requestCode = requestCode;
    }

    public Observable getObservable(Retrofit retrofit) {
        if (requestCallback == null)
            throw new IllegalArgumentException("requestCallback can not be null!");
        return requestCallback.getObservable(retrofit);
    }

    public Context getRequestContext() {
        if (rxAppCompatActivity != null)
            return rxAppCompatActivity.get();
        return null;
    }

    public HttpResultCallback getCallback() {
        if (resultCallback != null)
            return resultCallback.get();
        return null;
    }

    public BaseHttpParam setCallback(SoftReference<HttpResultCallback> callback) {
        this.resultCallback = callback;
        return this;
    }

    public boolean isCancel() {
        return cancel;
    }

    public BaseHttpParam setCancel(boolean cancel) {
        this.cancel = cancel;
        return this;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public BaseHttpParam setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
        return this;
    }

    public long getConnectionTime() {
        return connectionTime;
    }

    public BaseHttpParam setConnectionTime(long connectionTime) {
        this.connectionTime = connectionTime;
        return this;
    }

    public boolean isNeedCommonReqParam() {
        return needCommonReqParam;
    }

    public BaseHttpParam setNeedCommonReqParam(boolean needCommonReqParam) {
        this.needCommonReqParam = needCommonReqParam;
        return this;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public BaseHttpParam setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public boolean isNeedToken() {
        return needToken;
    }

    public BaseHttpParam setNeedToken(boolean needSessionId) {
        this.needToken = needSessionId;
        return this;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public abstract HttpSubscriberClient getSubscriberClient();

    public abstract String getBaseUrl();

    public BaseHttpParam setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public Object getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Object extraInfo) {
        this.extraInfo = extraInfo;
    }
}
