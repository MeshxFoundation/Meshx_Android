package com.pgy.meshim.activity

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import com.pgy.meshim.R
import com.pgy.meshim.fragment.factory.DefaultViewPagerFragmentAdapter
import com.pgy.meshim.fragment.factory.FragmentFactory
import com.pgy.meshim.services.LoginService
import com.pgy.meshim.utils.db.DbUserUtils
import com.pgy.meshim.views.home.HomeIndicatorView
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Created by lzy on 2018/6/11.
 */
class MainActivity : BaseActivity(), HomeIndicatorView.IndicatorCallback {

    private val tabIconIds = intArrayOf(R.drawable.tab01_textview_selector,
            R.drawable.tab02_textview_selector,
            R.drawable.tab03_textview_selector,
            R.drawable.tab04_textview_selector,
            R.drawable.tab05_textview_selector)
    private val tabTitles = arrayOf(R.string.main_fragment_home,
            R.string.main_fragment_people,
            R.string.main_fragment_wallet,
            R.string.main_fragment_discovery,
            R.string.main_fragment_me)

    private lateinit var adapter: DefaultViewPagerFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        goBack = false

        adapter = DefaultViewPagerFragmentAdapter(supportFragmentManager, FragmentFactory.FRAGMENT_MAIN)
        mainViewPager.adapter = adapter
        mainViewPager.setIndicators(addIndicatorView())

        if (!appContext.loginUser.isLogin)
            DbUserUtils.getLastLoginUser(appContext) {
                it?.apply {
                    LoginService.start(this@MainActivity, userPassName, userPassWord)
                } ?: goToLogin()
            }
    }

    private fun addIndicatorView(): Array<HomeIndicatorView?> {

        val params = LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        params.weight = 1f
        val tabNum = tabIconIds.size

        val indicators = arrayOfNulls<HomeIndicatorView>(tabNum)
        for (i in 0 until tabNum) {
            val indicatorView = HomeIndicatorView(this)
            indicatorView.index = i
            indicatorView.setIndicatorImage(tabIconIds[i])
            indicatorView.setIndicatorText(resources.getString(tabTitles[i]))
            indicatorView.setIndicatorCallback(this)
            groupTab.addView(indicatorView, params)
            if (i == mainViewPager.currentItem)
                indicatorView.showIndicator()
            indicators[i] = indicatorView
        }
        return indicators
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onSelect(index: Int) {
        (0 until groupTab.childCount)
                .map { groupTab.getChildAt(it) }
                .filterIsInstance<HomeIndicatorView>()
                .forEach { it.hideIndicator() }

        mainViewPager.setCurrentItem(index, false)
    }

}
