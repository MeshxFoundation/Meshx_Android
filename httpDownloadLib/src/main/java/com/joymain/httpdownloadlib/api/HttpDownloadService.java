package com.joymain.httpdownloadlib.api;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by linzh on 2017/5/4.
 */

public interface HttpDownloadService {
    @Streaming
    @GET
    Observable<ResponseBody> getFile(@Url String url);
}
