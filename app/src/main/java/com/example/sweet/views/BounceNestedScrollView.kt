package com.example.sweet.views

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.TranslateAnimation
import androidx.core.widget.NestedScrollView

/**
 *author：lm
 *create：2022/6/16 15:48
 *project：Sweet
 *package：com.example.sweet.views
 *description：阻尼效果的NestedScrollView
 */
class BounceNestedScrollView(context: Context, attrs: AttributeSet?) :NestedScrollView(context,attrs){

    private lateinit var mOnActionUpListener: OnActionUpListener

    private lateinit var mInnerView: View

    private var  mRect = Rect()

    // y方向上当前触摸点的前一次记录位置
    private var previousY =0;
   // y方向上的触摸点的起始记录位置
    private var startY =0;
   // y方向上的触摸点当前记录位置
    private var currentY =0
   // y方向上两次移动间移动的相对距离
   private var deltaY =0
    //水平移动搞定距离
    private var moveHeight = 0f

    override fun onFinishInflate() {
        super.onFinishInflate()
        //获取的就是 scrollview 的第一个子 View
        if (childCount > 0) {
            mInnerView = getChildAt(0)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (mInnerView == null) {
            return super.dispatchTouchEvent(ev)
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {

                startY = ev.y.toInt()
                previousY = startY
                // 记录childView的初始位置
                mRect.set(
                    mInnerView.left, mInnerView.top,

                    mInnerView.right, mInnerView.bottom
                )
                moveHeight = 0F
            }
            MotionEvent.ACTION_MOVE -> {
                currentY = ev.y.toInt()
                deltaY = currentY - previousY
                previousY = currentY
                //判定是否在顶部或者滑到了底部
                if ((!mInnerView.canScrollVertically(-1) && (currentY -startY) >0) ||
                    (!mInnerView.canScrollVertically(1) && (currentY -startY) <0)) {
                    //计算阻尼
                    var distance =currentY -startY
                    if (distance <0) {
                        distance *= -1
                    }
                    var damping =0.5f//阻尼值
                    val height = height
                    if (height !=0) {
                        damping = if (distance > height) {
                            0F
                        }else {
                            ((height - distance) / height).toFloat()
                        }
                    }
                    if (currentY -startY <0) {
                        damping =1 - damping
                    }
                    //阻力值限制再0.3-0.5之间，平滑过度
                    damping *=0.25F
                    damping+=0.25F
                    moveHeight += (deltaY * damping)

                    mInnerView.layout(mRect.left, ((mRect.top +moveHeight).toInt()),mRect.right,
                        ((mRect.bottom +moveHeight).toInt()))
                }
            }
            MotionEvent.ACTION_UP -> {
                //保证手指是移动后离开的
                if(currentY!=0){
                    mOnActionUpListener.onActionUpListener(currentY -startY)
                }
                if (isNeedAnimation()) {
                    translateAnimator()
                }
                //重置一些参数
                startY =0
                currentY =0
                mRect.setEmpty()

            }
        }
        return super.dispatchTouchEvent(ev)
    }
    private fun translateAnimator() {
        val animation =  TranslateAnimation(0F, 0F, mInnerView.top.toFloat(), mRect.top.toFloat())
        animation.duration = 600
        animation.fillAfter = true
        //设置阻尼动画效果
        animation.interpolator = DampInterpolator()
        mInnerView.startAnimation(animation)
        // 设置回到正常的布局位置
        mInnerView.layout(mRect.left, mRect.top, mRect.right, mRect.bottom)
    }

    // 是否需要开启动画
    private fun isNeedAnimation():Boolean {
        return !mRect.isEmpty
    }

    inner class DampInterpolator: Interpolator {

        override fun getInterpolation(input: Float): Float {
            //先快后慢，为了更快更慢的效果，多乘了几次，现在这个效果比较满意
            return 1 - (1 - input) * (1 - input) * (1 - input) * (1 - input) * (1 - input)
        }

    }
    fun setOnActionUpListener(onActionUpListener: OnActionUpListener){
        this.mOnActionUpListener=onActionUpListener
    }

    /**
     * 手指离开界面的监听
     */
    interface OnActionUpListener{
        fun onActionUpListener(delta:Int)
    }
}