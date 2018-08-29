package com.joymain.httpdownloadlib.callback;

/**
 * Created by lzy on 2017-4-27.
 */
//网络请求回调结果
public interface HttpResultCallback {

    /**
     * 网络请求成功后回调结果
     *
     * @param o           通过Gson解析出来的对象
     * @param requestCode 请求码
     */
    void onHttpSuccess(Object o, int requestCode, Object extraInfo);

    /**
     * 失败或者错误方法
     *
     * @param msg 错误信息
     */
    void onHttpError(int requestCode, String msg, Object extraInfo);

}
