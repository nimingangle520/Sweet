package com.example.sweet.test

import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.drake.engine.base.EngineActivity
import com.drake.statusbar.immersive
import com.example.sweet.R
import com.example.sweet.databinding.ActivityWeixinHeadBinding
import kotlinx.android.synthetic.main.activity_head_scroll.*


/**
 *author：lm
 *create：2022/6/28 16:32
 *project：Sweet
 *package：com.example.sweet.test
 *description：头部图片缩放、展开
 */
class HeadScrollActivity :EngineActivity<ActivityWeixinHeadBinding>(R.layout.activity_head_scroll){

    override fun initData() {

    }

    override fun initView() {
        immersive()

    }
}