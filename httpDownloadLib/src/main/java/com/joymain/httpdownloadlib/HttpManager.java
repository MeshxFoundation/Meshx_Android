package com.joymain.httpdownloadlib;

import android.content.Context;
import android.util.Log;

import com.joymain.httpdownloadlib.converter.StringConverterFactory;
import com.joymain.httpdownloadlib.exception.RetryWhenNetworkException;
import com.joymain.httpdownloadlib.param.BaseHttpParam;
import com.joymain.httpdownloadlib.subscriber.HttpSubscriberClient;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lzy on 2017-4-27.
 * Http请求管理类
 */
public class HttpManager {
    private volatile static HttpManager INSTANCE;

    //构造方法私有
    private HttpManager() {
    }

    //获取单例
    public static HttpManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpManager();
                }
            }
        }
        return INSTANCE;
    }


    public HttpSubscriberClient httpRequest(BaseHttpParam param) {
        Retrofit retrofit = createHttpRetrofit(param);

        Observable observable = param.getObservable(retrofit)
                .retryWhen(new RetryWhenNetworkException())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());

        Context context = param.getRequestContext();
        if (context instanceof RxAppCompatActivity) {
            RxAppCompatActivity appCompatActivity = (RxAppCompatActivity) context;
            observable.compose(appCompatActivity.bindUntilEvent(ActivityEvent.PAUSE));
        }

        HttpSubscriberClient client = param.getSubscriberClient();
        observable.subscribe(client);
        return client;
    }

    public Retrofit createHttpRetrofit(BaseHttpParam param) {
        //创建Http请求
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        httpBuilder.connectTimeout(param.getConnectionTime(), TimeUnit.SECONDS);
        httpBuilder.readTimeout(param.getConnectionTime(), TimeUnit.SECONDS);
        httpBuilder.writeTimeout(param.getConnectionTime(), TimeUnit.SECONDS);

        //根据需求添加公共参数
        if (param.isNeedCommonReqParam()) {
            BasicParamsInterceptor.Builder builder = new BasicParamsInterceptor.Builder();

            //是否添加token
//            if (param.isNeedToken())
//                builder.addQueryParam("token", param.getUserToken());

            //是否分页
            if (param.getPage() > 0) {
                builder.addQueryParam("pageNum", String.valueOf(param.getPage()));
                builder.addQueryParam("pageSize", String.valueOf(BaseHttpParam.PAGE_SIZE));
            }

            httpBuilder.addInterceptor(builder.build());
        }

        //Http Log类-----start
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("Http", message);
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpBuilder.addInterceptor(logInterceptor);
        //Http Log类-----end

        //创建Retrofit
        return new Retrofit.Builder()
                .baseUrl(param.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())//Gson转换成实体类格式
                .addConverterFactory(StringConverterFactory.create())//转换成String格式
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpBuilder.build())
                .build();
    }
}
