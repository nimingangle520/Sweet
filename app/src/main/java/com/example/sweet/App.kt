package com.example.sweet

import android.app.Application
import com.drake.engine.base.Engine
import com.drake.tooltip.ToastConfig

/**
 *author：lm
 *create：2022/6/16 10:09
 *project：Sweet
 *package：com.example.sweet
 *description：
 */
class App :Application (){
    override fun onCreate() {
        super.onCreate()
        ToastConfig.initialize(this)
        Engine.initialize(this)
    }
}