package com.joymain.httpdownloadlib.subscriber;


import android.util.Log;

import com.joymain.httpdownloadlib.HttpDownloadManager;
import com.joymain.httpdownloadlib.callback.HttpProgressCallback;
import com.joymain.httpdownloadlib.callback.OkHttpInterceptCallback;
import com.joymain.httpdownloadlib.param.HttpDownParam;

import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by linzh on 2017/5/4.
 */

public class HttpDownSubscriberClient<T> extends Subscriber<T> implements OkHttpInterceptCallback {

    private final static String TAG = "HttpDownSubscriber";
    private SoftReference<HttpProgressCallback> proCallback;
    private HttpDownParam param;

    public HttpDownSubscriberClient(HttpDownParam param) {
        this.param = param;
        proCallback = new SoftReference<>(param.getProCallback());
    }

    @Override
    public void onStart() {
        if (proCallback.get() != null)
            proCallback.get().onStart();

    }

    @Override
    public void onCompleted() {
        Log.i(TAG, "onCompleted");
        if (proCallback.get() != null) {
            proCallback.get().onComplete();
        }
        HttpDownloadManager.getInstance().removeDownload(param);
    }

    @Override
    public void onError(Throwable e) {
        Log.i(TAG, "onError");
        if (proCallback.get() != null) {
            proCallback.get().onError(e);
        }
        HttpDownloadManager.getInstance().removeDownload(param);
    }

    @Override
    public void onNext(T t) {
        Log.i(TAG, "onNext");
        if (proCallback.get() != null) {
            proCallback.get().onNext(t);
        }
    }

    private Subscriber<? super Long> proSubscriber;

    @Override
    public void update(long read, long count, boolean done) {
        Log.i(TAG, "read-->" + read + "--count-->" + count + "--done-->" + done);
        if (param.getCountLength() > count) {
            read = param.getCountLength() - count + read;
        } else {
            param.setCountLength(count);
        }
        param.setReadLength(read);

        if (proCallback.get() == null) return;

        if (proSubscriber == null) {

            rx.Observable.create(new Observable.OnSubscribe<Long>() {
                @Override
                public void call(Subscriber<? super Long> subscriber) {
                    proSubscriber = subscriber;
                }
            }).debounce(10, TimeUnit.MILLISECONDS)//每10ms发送一条更新指令
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            proCallback.get().updateProgress(aLong, param.getCountLength());
                        }
                    });
        } else proSubscriber.onNext(read);
    }
}
