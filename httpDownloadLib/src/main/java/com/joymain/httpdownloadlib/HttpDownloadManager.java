package com.joymain.httpdownloadlib;

import android.text.TextUtils;
import android.util.Log;

import com.joymain.httpdownloadlib.api.HttpDownloadService;
import com.joymain.httpdownloadlib.callback.DownloadInterceptor;
import com.joymain.httpdownloadlib.exception.RetryWhenNetworkException;
import com.joymain.httpdownloadlib.param.HttpDownParam;
import com.joymain.httpdownloadlib.subscriber.HttpDownSubscriberClient;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by linzh on 2017/5/4.
 * http下载管理器
 */

public class HttpDownloadManager {

    private final static String TAG = "HttpDownloadManager";

    /*下载列表*/
    private Set<HttpDownParam> infos;
    /*下载器Map*/
    private Map<String, HttpDownSubscriberClient> subMap;

    private volatile static HttpDownloadManager INSTANCE;

    private HttpDownloadManager() {
        infos = new HashSet<>();
        subMap = new HashMap<>();
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static HttpDownloadManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpDownloadManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDownloadManager();
                }
            }
        }
        return INSTANCE;
    }

    //开始下载
    public void download(final HttpDownParam param) {
        if (param == null || TextUtils.isEmpty(param.getUrl())) return;
        String downloadUrl = param.getUrl();
        //判断下载路径是否已经存在下载列表中
        if (subMap.containsKey(downloadUrl)) return;

        HttpDownSubscriberClient<HttpDownParam> downSubscriber = new HttpDownSubscriberClient<>(param);

        subMap.put(param.getUrl(), downSubscriber);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(param.getConnectionTime(), TimeUnit.SECONDS)
                .addInterceptor(new DownloadInterceptor(downSubscriber))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getBasUrl(downloadUrl))
                .build();

        retrofit.create(HttpDownloadService.class)
                .getFile(param.getUrl())
                .retryWhen(new RetryWhenNetworkException())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, HttpDownParam>() {
                         @Override
                         public HttpDownParam call(ResponseBody body) {
                             Log.i(TAG, "write2File------start");
                             try {
                                 write2File(body, new File(param.getSavePath()), param);
                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                             return param;
                         }
                     }
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(downSubscriber);
    }

    /**
     * 写入文件
     *
     * @param file  文件路径
     * @param param 下载文件信息
     * @throws IOException
     */
    private void write2File(ResponseBody responseBody, File file, HttpDownParam param) throws IOException {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        long allLength = param.getCountLength() == 0 ? responseBody.contentLength() :
                param.getCountLength();

        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
        FileChannel channelOut = randomAccessFile.getChannel();

        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE,
                param.getReadLength(), allLength - param.getReadLength());
        byte[] buffer = new byte[1024 * 8];
        int len;
        while ((len = responseBody.byteStream().read(buffer)) != -1) {
            mappedBuffer.put(buffer, 0, len);
        }

        responseBody.byteStream().close();

        channelOut.close();

        randomAccessFile.close();
    }

    /**
     * 获取Url中的BaseUrl
     *
     * @param url 总体url
     * @return baseUrl
     */
    public String getBasUrl(String url) {
        String head = "";
        int index = url.indexOf("://");
        if (index != -1) {
            head = url.substring(0, index + 3);
            url = url.substring(index + 3);
        }
        index = url.indexOf("/");
        if (index != -1) {
            url = url.substring(0, index + 1);
        }
        return head + url;
    }

    //完成时 移除下载任务
    public void removeDownload(HttpDownParam param) {
        if (param == null) return;
        infos.remove(param);
        subMap.remove(param.getUrl());
    }

    public void stopDownload(HttpDownParam param) {
        if (param == null) return;
        //不存在的任务
        if (!subMap.containsKey(param.getUrl())) return;
        HttpDownSubscriberClient subscribers = subMap.get(param.getUrl());
        subscribers.unsubscribe();
    }

    public boolean isDownloading(String url) {
        if (subMap == null || subMap.size() == 0) return false;
        return subMap.containsKey(url);
    }

}
