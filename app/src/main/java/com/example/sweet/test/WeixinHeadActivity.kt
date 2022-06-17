package com.example.sweet.test

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.drake.engine.base.EngineActivity
import com.drake.engine.utils.*
import com.drake.statusbar.immersive
import com.drake.statusbar.statusPadding
import com.example.sweet.R
import com.example.sweet.databinding.ActivityWeixinHeadBinding
import com.example.sweet.utils.BitmapUtils
import com.example.sweet.views.BounceNestedScrollView
import kotlinx.android.synthetic.main.activity_main.toolBar
import kotlinx.android.synthetic.main.activity_weixin_head.*
import kotlinx.android.synthetic.main.layout_toolbar.*


/**
 *author：lm
 *create：2022/6/15 19:24
 *project：Sweet
 *package：com.example.sweet.test
 *description：微信头部
 */
open class WeixinHeadActivity :EngineActivity<ActivityWeixinHeadBinding>(R.layout.activity_weixin_head){

    //是否是展开状态
    private var isExpended=false

    override fun initData() {

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initView() {
        immersive()
        toolBar.statusPadding()
        actionTitle.text=getString(R.string.weixin_head)
        actionTitle.setTextColor(ContextCompat.getColor(this,R.color.white))
        actionLeft.throttleClick { finish() }

        //图片初始高度
        val params=ivPic.layoutParams as RelativeLayout.LayoutParams
        params.height=(ScreenUtils.getScreenHeight()*0.45).toInt()
        //模糊区域高度
        val paramsTop=ivTopBlur.layoutParams as RelativeLayout.LayoutParams
        paramsTop.height=(ScreenUtils.getScreenHeight()*0.17).toInt()
        val paramsBottom=ivBottomBlur.layoutParams as RelativeLayout.LayoutParams
        paramsBottom.height=(ScreenUtils.getScreenHeight()*0.17).toInt()
        //点击图片
        ivPic.throttleClick {
            /*val params=ivPic.layoutParams as RelativeLayout.LayoutParams
            params.height=if(!isExpended) (ScreenUtils.getScreenHeight()*0.8).toInt() else (ScreenUtils.getScreenHeight()*0.3).toInt()
            isExpended=!isExpended
            ivPic.requestLayout()*/
            startAnimatorSet()
        }

        nsView.setOnActionUpListener(object :BounceNestedScrollView.OnActionUpListener{
            override fun onActionUpListener(delta: Int) {
                if(isExpended){
                    if(delta<=-200){
                        startAnimatorSet()
                        nsView.scrollTo(0,delta)
                    }else{
                        nsView.scrollTo(0,0)
                    }
                }
            }
        })
    }

    /**
     * 动态改变高度属性动画
     */
    private fun changeHeightAnimation():ValueAnimator{
        //属性动画对象
        val valueAnimation:ValueAnimator = if(isExpended){
            ValueAnimator.ofInt((ScreenUtils.getScreenHeight()*0.8).toInt(),(ScreenUtils.getScreenHeight()*0.45).toInt())
        }else{
            ValueAnimator.ofInt((ScreenUtils.getScreenHeight()*0.45).toInt(),(ScreenUtils.getScreenHeight()*0.8).toInt())
        }
        valueAnimation.addUpdateListener {
            val height=it.animatedValue as Int
            val params=ivPic.layoutParams as RelativeLayout.LayoutParams
            params.height=height
            ivPic.requestLayout()
        }
        valueAnimation.addListener(object :Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                ivTopBlur.visibility=if(isExpended) View.VISIBLE else  View.GONE
                ivBottomBlur.visibility=if(isExpended) View.VISIBLE else  View.GONE
                if(ivTopBlur.visibility==View.VISIBLE)setBlur()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }

        })
        isExpended=!isExpended
        return valueAnimation
    }

    /**
     * 设置区域模糊
     */
    protected fun setBlur() {
        val vto: ViewTreeObserver = ivPic.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // 保证只调用一次
                ivPic.viewTreeObserver.removeOnGlobalLayoutListener(this)
                // 组件生成cache（组件显示内容）
                ivPic.buildDrawingCache()
                // 得到组件显示内容
                var bitmap:Bitmap= ivPic.getDrawingCache()
                BitmapUtils.blur(applicationContext, bitmap, ivTopBlur, 8)
                BitmapUtils.blur(applicationContext, bitmap, ivBottomBlur, 8)
            }
        })
    }

    /**
     * 开始改变透明度的动画
     */
    private fun changeAlphaAnimation():ValueAnimator{
        //属性动画对象
        val valueAnimation:ValueAnimator = if(isExpended){
            ValueAnimator.ofFloat(0F,0.3F,0.5F,0.7F,0.9F,1F)
        }else{
            ValueAnimator.ofFloat(1F,0.9F,0.7F,0.5F,0.3F,0F)
        }
        valueAnimation.addUpdateListener {
            val alpha=it.animatedValue as Float
            ivTopBlur.alpha= alpha
            ivBottomBlur.alpha= alpha
        }
        return valueAnimation
    }

    /**
     * 开始组合动画
     */
    private fun startAnimatorSet(){
        val animator=AnimatorSet()
        animator.playTogether(changeHeightAnimation(),changeAlphaAnimation())
        animator.duration=300
        animator.start()
    }
}