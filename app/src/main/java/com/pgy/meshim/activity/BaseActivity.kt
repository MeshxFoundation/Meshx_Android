package com.pgy.meshim.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.pgy.meshim.MeshApplication
import com.pgy.meshim.R
import com.pgy.meshim.utils.*
import com.pgy.xmpplib.callback.XmppResultCallback
import com.pgy.xmpplib.service.XmppParams


/**
 * Created by lzy on 2017/11/29.
 */

open class BaseActivity : AppCompatActivity(), XmppResultCallback {

    val appContext: MeshApplication
        get() = CommonUtils.getApplication(this)

    val screenHeight: Int
        get() = windowManager.defaultDisplay.height

    val appPref: AppPref by lazy { AppPref(this) }

    private var permissionCallback: PermissionRequestCallback? = null
    private val PERMISSION_REQUEST_CODE = 0x501
    private var requestCode: Int = 0

    private var permissionUtil: PermissionUtil? = null

    protected lateinit var toolBar: View

    //---------------toolbar-----------------------

//    @RequiresApi(Build.VERSION_CODES.M)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//    }

    protected fun initToolBar(resString: Int, canBack: Boolean = true) {
        initToolBar(resources.getString(resString), canBack)
    }

    protected fun initToolBar(title: String, canBack: Boolean = true) {
        initToolBar(title, canBack, -1, null)
    }

    protected fun initToolBar(titleId: Int, canBack: Boolean, rightImgRes: Int, clickCallback: ((View) -> Unit)?) {
        initToolBar(resources.getString(titleId), canBack, rightImgRes, clickCallback)
    }

    protected fun initToolBar(title: String, canBack: Boolean, rightImgRes: Int, clickCallback: ((View) -> Unit)?) {
        goBack = canBack

        toolBar = findViewById(R.id.toolbar)

        findViewById<TextView>(R.id.tv_title).text = title

        if (canBack) findViewById<View>(R.id.btn_back).setOnClickListener {
            onBackPressed()
        }
        else findViewById<View>(R.id.btn_back).visibility = View.INVISIBLE

        if (rightImgRes > 0) {
            findViewById<ImageView>(R.id.iv_right).apply {
                visibility = View.VISIBLE
                setImageResource(rightImgRes)
                setOnClickListener {
                    clickCallback?.invoke(it)
                }
            }
        }
    }

//    fun initStatusBar(color: Int) {
//        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        //设置状态栏颜色
//        window.statusBarColor = color
//    }

    protected fun showShortToast(txt: String) {
        ToastUtil.show(this, txt)
    }

    protected fun showShortToast(source: Int) {
        ToastUtil.show(this, source)
    }

    protected fun hideSoftInput(v: View) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    protected fun showSoftInput(v: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!imm.isActive(v)) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    protected fun copyToClipboard(text: String) {
        //获取剪贴板管理器：
        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 创建普通字符型ClipData
        val mClipData = ClipData.newPlainText("Label", text)
        // 将ClipData内容放到系统剪贴板里。
        cm.primaryClip = mClipData
    }

    //---------------默认Http请求回调---------------------

    override fun onSuccess(params: XmppParams, result: Any) {

    }

    override fun onFailed(params: XmppParams, errorCode: Int, msg: String) {
        showShortToast(msg)
    }


    override fun onCancel(params: XmppParams) {
        showShortToast(R.string.user_cancel)
    }

    fun registerPermissionCallback(callback: PermissionRequestCallback) {
        this.permissionCallback = callback
    }

    //请求权限
    fun requestPermission(requestCode: Int, vararg permissionArray: String) {
        this.requestCode = requestCode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0以上的手机系统需要检测权限问题
            if (permissionArray.isNotEmpty()) {//需要权限
                if (permissionUtil == null) permissionUtil = PermissionUtil(this)
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
        val intent = Intent(this, PermissionActivity::class.java)
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

    open protected fun onPermissionRequestCallBack(requestCode: Int, hasPermission: Boolean) {

    }

    interface PermissionRequestCallback {
        fun onPermissionCallback(requestCode: Int, hasPermission: Boolean): Boolean
    }
    //---------------权限请求---------------------

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //拒绝时，没有获取到主要权限
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (resultCode == PermissionActivity.PERMISSION_DENIED)
                dispatchPermissionCallBack(false)
            else
                dispatchPermissionCallBack(true)
        }
    }


    //exit app
    private var mExitTime: Long = 0
    protected var goBack: Boolean = true

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        goBack.takeIf { !it && (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) }?.let {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (System.currentTimeMillis() - mExitTime > 2000) {
                    showShortToast(R.string.exit_app_hint)
                    mExitTime = System.currentTimeMillis()
                } else {
                    val startMain = Intent(Intent.ACTION_MAIN)
                    startMain.addCategory(Intent.CATEGORY_HOME)
                    startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(startMain)
                    System.exit(0)
                }
            }
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ResiurceWraUtils.wrapForSp(newBase))
    }
}
