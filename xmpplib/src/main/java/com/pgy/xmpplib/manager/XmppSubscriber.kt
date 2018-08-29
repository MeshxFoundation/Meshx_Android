package com.pgy.xmpplib.manager

import android.app.ProgressDialog
import android.content.Context
import com.pgy.xmpplib.R
import com.pgy.xmpplib.callback.XmppResultCallback
import com.pgy.xmpplib.exception.NetWorkException
import com.pgy.xmpplib.service.XmppParams
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.sasl.SASLErrorException
import java.lang.ref.SoftReference

/**
 * Created by lzy on 2018/6/14.
 */
class XmppSubscriber(context: Context, resultCallback: XmppResultCallback, private val params: XmppParams) : Observer<Any?> {

    private lateinit var disposable: Disposable

    private val contextReference = SoftReference<Context>(context)
    private val callbackReference = SoftReference<XmppResultCallback>(resultCallback)

    private var progressDialog: ProgressDialog? = null

    override fun onComplete() {
        progressDialog?.dismiss()
    }

    override fun onSubscribe(d: Disposable) {
        disposable = d
        if (params.showWaitingDialog)
            contextReference.get()?.apply {
                progressDialog = ProgressDialog.show(this, "", getWaitingInfoByRequestCode(this))

            }
    }

    private fun getWaitingInfoByRequestCode(context: Context) =
            context.getString(
                    when (params.requestCode) {
                        XmppRequestCode.LOGIN -> R.string.loading_login
                        XmppRequestCode.REGISTER -> R.string.loading_register
                        XmppRequestCode.ADD_FRIENDS -> R.string.loading_add
                        XmppRequestCode.DELETE_FRIENDS -> R.string.loading_delete
                        XmppRequestCode.SEARCH_FRIENDS -> R.string.loading_search
                        else -> R.string.loading_default
                    }
            )


    override fun onNext(result: Any) {
        callbackReference.get()?.onSuccess(params, result)
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        progressDialog?.dismiss()

        callbackReference.get()?.apply {
            contextReference.get()?.apply {
                val errorRes = when {
                    params.requestCode == XmppRequestCode.LOGIN && e is SASLErrorException -> XmppResultCode.ERROR_CODE_LOGIN_PASSWORD to R.string.error_user_password
                    params.requestCode == XmppRequestCode.REGISTER && e is XMPPException.XMPPErrorException -> XmppResultCode.ERROR_CODE_REGISTER to R.string.error_register_passName
                    e is NetWorkException -> XmppResultCode.ERROR_CODE_NET to R.string.error_net_connection
                    else -> XmppResultCode.ERROR_CODE_UNKNOW to R.string.error_network
                }

                onFailed(params, errorRes.first, resources.getString(errorRes.second))
            }
        }
    }

}