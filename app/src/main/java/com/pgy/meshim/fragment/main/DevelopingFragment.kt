package com.pgy.meshim.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pgy.meshim.R
import com.pgy.meshim.fragment.BaseFragment

/**
 * Created by lzy on 2018/8/29.
 */
class DevelopingFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_developing, container, false)
}