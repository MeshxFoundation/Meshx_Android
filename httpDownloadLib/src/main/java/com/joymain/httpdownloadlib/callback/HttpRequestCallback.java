package com.joymain.httpdownloadlib.callback;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by Administrator on 2017-4-27.
 */
public interface HttpRequestCallback {
    Observable getObservable(Retrofit retrofit);
}
