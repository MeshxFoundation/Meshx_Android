package com.pgy.meshim.fragment


import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.Fragment
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.joymain.httpdownloadlib.callback.HttpResultCallback
import com.pgy.meshim.MeshApplication
import com.pgy.meshim.R
import com.pgy.meshim.activity.BaseActivity
import com.pgy.meshim.activity.PermissionActivity
import com.pgy.meshim.utils.AppPref
import com.pgy.meshim.utils.CommonUtils
import com.pgy.meshim.utils.PermissionUtil
import com.pgy.meshim.utils.ToastUtil
import com.pgy.xmpplib.callback.XmppResultCallback
import com.pgy.xmpplib.service.XmppParams


/**
 * Fragment基类
 *
 * @author lzy
 */
open class BaseFragment : Fragment(), XmppResultCallback, HttpResultCallback {

    val appContext: MeshApplication
        get() = CommonUtils.getApplication(requireContext())

    val appPref: AppPref by lazy { AppPref(requireContext()) }

    protected fun getStatusBarHeight(): Float {
        var result = 0f
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimension(resourceId)
        }
        return result
    }   //返回值就是状态栏的高度,得到的值是像素

    protected fun showShortToast(txt: String) {
        ToastUtil.show(requireContext(), txt)
    }

    protected fun showShortToast(source: Int) {
        ToastUtil.show(requireContext(), source)
    }

    protected fun hideSoftInput(v: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    protected fun initToolBar(rootView: View, resString: Int, canBack: Boolean) {
        initToolBar(rootView, resources.getString(resString), canBack)
    }

    protected fun initToolBar(rootView: View, title: String, canBack: Boolean) {
        initToolBar(rootView, title, canBack, -1, null)
    }

    protected fun initToolBar(rootView: View, titleId: Int, canBack: Boolean, rightImgRes: Int, clickCallback: ((View) -> Unit)?) {
        initToolBar(rootView, resources.getString(titleId), canBack, rightImgRes, clickCallback)
    }

    protected fun initToolBar(rootView: View, title: String, canBack: Boolean, rightImgRes: Int, clickCallback: ((View) -> Unit)?) {
        rootView.findViewById<TextView>(R.id.tv_title).text = title

        if (canBack) rootView.findViewById<View>(R.id.btn_back).setOnClickListener {
            activity?.onBackPressed()
        }
        else rootView.findViewById<View>(R.id.btn_back).visibility = View.INVISIBLE

        if (rightImgRes > 0) {
            rootView.findViewById<ImageView>(R.id.iv_right).apply {
                visibility = View.VISIBLE
                setImageResource(rightImgRes)
                setOnClickListener {
                    clickCallback?.invoke(it)
                }
            }
        }
    }

    fun initStatusBar(color: Int) {
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //设置状态栏颜色
        activity?.window?.statusBarColor = color
    }

    //---------------默认Xmpp请求回调---------------------

    override fun onSuccess(params: XmppParams, result: Any) {

    }

    override fun onFailed(params: XmppParams, errorCode: Int, msg: String) {
        showShortToast(msg)
    }


    override fun onCancel(params: XmppParams) {
        showShortToast(R.string.user_cancel)
    }

    //---------------权限请求---------------------
    private var permissionCallback: BaseActivity.PermissionRequestCallback? = null
    private val PERMISSION_REQUEST_CODE = 0x501
    private var requestCode: Int = 0

    private var permissionUtil: PermissionUtil? = null

    fun registerPermissionCallback(callback: BaseActivity.PermissionRequestCallback) {
        this.permissionCallback = callback
    }

    //请求权限
    fun requestPermission(requestCode: Int, vararg permissionArray: String) {
        this.requestCode = requestCode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0以上的手机系统需要检测权限问题
            if (permissionArray.isNotEmpty()) {//需要权限
                if (permissionUtil == null) permissionUtil = PermissionUtil(context!!)
                if (permissionUtil!!.permissionSet(*permissionArray)) {//拒绝了权限，提示用户开启权限
                    startPermissionActivity(*permissionArray)
                    return
                }
            }
        }
        dispatchPermissionCallBack(true)
    }

    //进入权限设置页面
    private fun startPermissionActivity(vararg permissionArray: String) {
        val intent = Intent(context, PermissionActivity::class.java)
        intent.putExtra(PermissionActivity.EXTRA_PERMISSION, permissionArray)
        startActivityForResult(intent, PERMISSION_REQUEST_CODE)
    }

    protected fun dispatchPermissionCallBack(hasPermission: Boolean) {
        if (permissionCallback != null) {
            if (permissionCallback!!.onPermissionCallback(requestCode, hasPermission))
                return
        }
        onPermissionRequestCallBack(requestCode, hasPermission)
    }

    protected fun onPermissionRequestCallBack(requestCode: Int, hasPermission: Boolean) {

    }
    //---------------权限请求---------------------

    protected fun isLogin() = appContext.loginUser.isLogin
    open fun onLoginSuccess() {}

    override fun onHttpSuccess(o: Any?, requestCode: Int, extraInfo: Any?) {

    }

    override fun onHttpError(requestCode: Int, msg: String?, extraInfo: Any?) {
        msg?.apply { showShortToast(this) }
    }

}
