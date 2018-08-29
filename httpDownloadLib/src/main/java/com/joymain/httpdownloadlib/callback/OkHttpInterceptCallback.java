package com.joymain.httpdownloadlib.callback;

/**
 * Created by linzh on 2017/5/4.
 * 监听OkHttp的下载进度，回调
 */

public interface OkHttpInterceptCallback {

    /**
     * 下载进度
     *
     * @param read  下载大小
     * @param count 总大小
     * @param done  是否完成
     */
    void update(long read, long count, boolean done);

}
