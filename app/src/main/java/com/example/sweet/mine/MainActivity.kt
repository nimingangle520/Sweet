package com.example.sweet.mine

import com.drake.engine.base.EngineActivity
import com.drake.statusbar.immersive
import com.drake.statusbar.statusPadding
import com.example.sweet.R

import com.example.sweet.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : EngineActivity<ActivityMainBinding>(R.layout.activity_main){

    override fun initData() {
    }

    override fun initView() {
        immersive(toolBar,darkMode = true)
        toolBar.statusPadding()
        actionTitle.text=getString(R.string.mine)
        actionLeft.setOnClickListener { finish() }
    }
}