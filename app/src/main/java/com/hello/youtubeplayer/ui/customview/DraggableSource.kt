package com.hello.youtubeplayer.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.isVisible
import com.hello.youtubeplayer.R
import com.hello.youtubeplayer.ui.main.MainActivity
import com.hello.youtubeplayer.ui.play.PlayVideoFragment
import com.hoanganhtuan95ptit.draggable.DraggablePanel
import com.hoanganhtuan95ptit.draggable.utils.inflate
import com.hoanganhtuan95ptit.draggable.utils.reWidth
import com.hoanganhtuan95ptit.draggable.utils.visible
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.layout_top.view.*

class DraggableSource @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : DraggablePanel(context, attrs, defStyleAttr) {


    var mWidthWhenMax = 0

    var mWidthWhenMiddle = 0

    var mWidthWhenMin = 0

    var playVideoFragment = PlayVideoFragment()

    init {
        getFrameFirst().addView(inflate(R.layout.layout_top))
        getFrameSecond().addView(inflate(R.layout.layout_bottom))
    }

    override fun initFrame() {
        mWidthWhenMax = width

        mWidthWhenMiddle = (width - mPercentWhenMiddle * mMarginEdgeWhenMin).toInt()

        mWidthWhenMin = mHeightWhenMinDefault * 22 / 9

        super.initFrame()
    }

    override fun refreshFrameFirst() {
        super.refreshFrameFirst()

        val width = if (mCurrentPercent < mPercentWhenMiddle) {
            (context as MainActivity).isCheckMaxScreen = true
            (mWidthWhenMax - (mWidthWhenMax - mWidthWhenMiddle) * mCurrentPercent)
        } else {
            (context as MainActivity).isCheckMaxScreen = false
            (mWidthWhenMiddle - (mWidthWhenMiddle - mWidthWhenMin) * (mCurrentPercent - mPercentWhenMiddle) / (1 - mPercentWhenMiddle))
        }
        frameTop.reWidth(width.toInt())

        ivClose.setOnClickListener(OnClickListener {
            draggablePanel.close()
            (context as MainActivity).pauseVideo()

        })

        ivPause.setOnClickListener(OnClickListener {
            (context as MainActivity).pauseVideo()
            ivPause.visibility = View.GONE
            ivPlay.visibility = View.VISIBLE
        })

        ivPlay.setOnClickListener(OnClickListener {
            (context as MainActivity).playVideo()
            ivPause.visibility = View.VISIBLE
            ivPlay.visibility = View.GONE
        })

        tvTitle.text = (context as MainActivity).titleVideo
        tvTitle.textSize = 14F
        //tvChannel.setText((context as MainActivity).nameChannel)

    }
}