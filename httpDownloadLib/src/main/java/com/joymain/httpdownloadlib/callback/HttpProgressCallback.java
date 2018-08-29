package com.joymain.httpdownloadlib.callback;

/**
 * Created by linzh on 2017/5/4.
 */

public interface HttpProgressCallback<T> {
    /**
     * 成功后回调方法
     *
     * @param t
     */
    void onNext(T t);

    /**
     * 开始下载
     */
    void onStart();

    /**
     * 完成下载
     */
    void onComplete();


    /**
     * 下载进度
     *
     * @param readLength  下载进度
     * @param countLength 总大小
     */
    void updateProgress(long readLength, long countLength);

    /**
     * 失败或者错误方法
     * 主动调用，更加灵活
     *
     * @param e
     */
    void onError(Throwable e);
}
