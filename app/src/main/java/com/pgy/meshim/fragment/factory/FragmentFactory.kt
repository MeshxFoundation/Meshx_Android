package com.pgy.meshim.fragment.factory

import com.pgy.meshim.fragment.BaseFragment
import com.pgy.meshim.fragment.main.DevelopingFragment
import com.pgy.meshim.fragment.main.MeFragment


/**
 * Created by lzy on 2017/12/01.
 * Fragment工厂类
 */

object FragmentFactory {

    const val FRAGMENT_MAIN = 1

    private const val HOME_FRAGMENT_INDEX = 0
    private const val PEOPLE_FRAGMENT_INDEX = 1
    private const val WALLET_FRAGMENT_INDEX = 2
    private const val DISCOVER_FRAGMENT_INDEX = 3
    private const val ME_FRAGMENT_INDEX = 4

    fun getFragmentArray(type: Int): Array<BaseFragment?> =
            when (type) {
                FRAGMENT_MAIN -> arrayOfNulls<BaseFragment?>(5)
                else -> arrayOf()
            }


    fun getFragment(type: Int, position: Int): BaseFragment? {
        var fragment: BaseFragment? = null
        when (type) {
            FRAGMENT_MAIN -> fragment = getMainFragment(position)
        }

        return fragment
    }

    private fun getMainFragment(position: Int): BaseFragment? {
        when (position) {
            ME_FRAGMENT_INDEX -> return MeFragment()
        }
        return DevelopingFragment()
    }

}
