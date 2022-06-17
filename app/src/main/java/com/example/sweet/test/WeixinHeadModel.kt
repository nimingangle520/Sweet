package com.example.sweet.test

import androidx.databinding.BaseObservable
import com.drake.brv.item.ItemPosition

/**
 * @ProjectName:    Sweet
 * @Package:  com.example.sweet
 * @Description:
 * @CreateDate：2022/3/18 17:32
 * @author lm
 * @version 1.0
 */
data class WeixinHeadModel (var url:Any?=null): BaseObservable(),ItemPosition{
    var modelName = "WeiXinModel"
    override var itemPosition: Int = 0
}