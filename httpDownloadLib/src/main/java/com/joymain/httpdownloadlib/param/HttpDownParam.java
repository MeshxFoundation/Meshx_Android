package com.joymain.httpdownloadlib.param;


import com.joymain.httpdownloadlib.callback.HttpProgressCallback;

/**
 * Created by linzh on 2017/5/4.
 * 下载文件配置参数
 */

public class HttpDownParam {
    /*存储位置*/
    private String savePath;
    /*文件总长度*/
    private long countLength;
    /*下载长度*/
    private long readLength;
    /*回调监听*/
    private HttpProgressCallback proCallback;
    /*超时设置*/
    private int connectionTime = 6;
    /*下载Url*/
    private String url;

    public HttpDownParam(String savePath, String url) {
        setSavePath(savePath);
        setUrl(url);
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public long getCountLength() {
        return countLength;
    }

    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }

    public long getReadLength() {
        return readLength;
    }

    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }

    public HttpProgressCallback getProCallback() {
        return proCallback;
    }

    public void setProCallback(HttpProgressCallback proCallback) {
        this.proCallback = proCallback;
    }

    public int getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(int connectonTime) {
        this.connectionTime = connectonTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
