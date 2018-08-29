package com.pgy.meshim.utils.db

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by lzy on 2018/7/16.
 */
open class BaseDbUtils {
    fun <T> dbAsyncTask(callback: ((T?) -> Unit)?, task: () -> T) {
        Observable.just(task).map {
            task.invoke()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<T> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: T) {
                        callback?.invoke(t)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        callback?.invoke(null)
                    }
                })
    }
}