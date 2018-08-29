package com.joymain.httpdownloadlib.subscriber;

import android.content.Context;
import android.support.annotation.NonNull;

import com.joymain.httpdownloadlib.R;
import com.joymain.httpdownloadlib.callback.HttpResultCallback;
import com.joymain.httpdownloadlib.param.BaseHttpParam;

import java.lang.ref.SoftReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * Created by lzy on 2017-4-27.
 */
public abstract class HttpSubscriberClient extends Subscriber {

    /*是否弹框*/
    protected boolean showProgress = true;
    /* 软引用回调接口*/
    protected SoftReference<HttpResultCallback> mCallback;
    /*软引用反正内存泄露*/
    protected SoftReference<Context> mActivity;
    /*加载框可自己定义*/
    protected WaitingDialog pd;
    //    /*请求数据*/
//    private HttpParam param;
    /*请求码*/
    protected int requestCode;

    protected Object extraInfo;

    public HttpSubscriberClient(BaseHttpParam param) {
        this.mCallback = new SoftReference<>(param.getCallback());
        this.mActivity = new SoftReference<>(param.getRequestContext());
        this.requestCode = param.getRequestCode();
        this.extraInfo = param.getExtraInfo();
        setShowProgress(param.isShowProgress());
        if (param.isShowProgress()) {
            initProgressDialog(param.isCancel());
        }
    }

    protected void initProgressDialog(boolean cancel) {
        final Context context = mActivity.get();
        if (pd == null && context != null) {
            pd = WaitingDialog.Companion.create(context, R.string.loading_default);
//            pd.setCancelable(cancel);
//            if (cancel) {
//                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialogInterface) {
//                        if (mCallback.get() != null) {
//                            mCallback.get().onError(requestCode, context.getString(R.string.error_user_cancel), extraInfo);
//                        }
//                        cancelProgress();
//                    }
//                });
//            }
        }
    }

    /**
     * 取消订阅关系，取消Http请求
     */
    protected void cancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }

    @Override
    public void onStart() {
        showProgressDialog();
    }

    /**
     * 显示加载框
     */
    protected void showProgressDialog() {
        if (!isShowProgress()) return;
        Context context = mActivity.get();
        if (pd == null || context == null) return;
        pd.show();
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();

        dismissProgressDialog();
        if (mCallback == null || mCallback.get() == null) return;
        if (e instanceof SocketTimeoutException) {
            onErrorCallback(mActivity.get().getString(R.string.error_net_work));
        } else if (e instanceof ConnectException) {
            onErrorCallback(mActivity.get().getString(R.string.error_net_work));
        } else {
            onErrorCallback(mActivity.get().getString(R.string.error_with_msg, e.getMessage()));
        }
    }

    @Override
    public void onNext(Object o) {
        if (o == null) {
            onErrorCallback(mActivity.get().getString(R.string.error_data_null));
            return;
        }

        onHttpRequestSuccess(o);
    }

    /**
     * Http请求成功回调,o已经做了非空判断
     *
     * @param o Retrofit中定义的实体
     */
    protected abstract void onHttpRequestSuccess(@NonNull Object o);

    protected void onErrorCallback(String msg) {
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().onHttpError(requestCode, msg, extraInfo);
        else
            android.util.Log.i("http","callback is null");
    }

    /**
     * 隐藏
     */
    protected void dismissProgressDialog() {
        if (!isShowProgress()) return;
        pd.dismiss();
    }

    protected void onSuccessCallback(Object o) {
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().onHttpSuccess(o, requestCode, extraInfo);
        else
            android.util.Log.i("http","callback is null");
    }
}
