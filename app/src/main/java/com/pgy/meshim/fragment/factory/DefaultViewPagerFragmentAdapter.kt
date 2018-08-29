package com.pgy.meshim.fragment.factory

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import com.pgy.meshim.fragment.BaseFragment

import com.pgy.meshim.fragment.factory.FragmentFactory


/**
 * Created by zz on 2017/5/25.
 * ViewPager适配器
 */

open class DefaultViewPagerFragmentAdapter(private val fm: FragmentManager, private val factoryType: Int) : FragmentPagerAdapter(fm) {
    val fragments: Array<BaseFragment?> = FragmentFactory.getFragmentArray(factoryType)

    override fun getPageTitle(position: Int): CharSequence {
        return ""
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position % fragments.size) as Fragment
        if (fragment.isHidden) {
            fm.beginTransaction().show(fragment).commit()
        }
        return fragment
    }

    override fun getItem(position: Int): Fragment? {
        val index = position % fragments.size
        if (fragments[index] != null) return fragments[index]

        val baseFragment = FragmentFactory.getFragment(factoryType, position)

        val bundle = getBundleToFragment(position)
        if (bundle != null)
            baseFragment?.arguments = bundle

        fragments[position] = baseFragment

        return baseFragment
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, o: Any) {
        fm.beginTransaction().hide(o as Fragment).commit()
    }

    open protected fun getBundleToFragment(position: Int): Bundle? {
        return null
    }
}
