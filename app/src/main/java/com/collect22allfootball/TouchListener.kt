package com.collect22allfootball

import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.collect22allfootball.utils.Target
import java.lang.Math.*


class TouchListener(private val screenWidth: Int,
                    private val screenHeight: Int,
                    private val targetLocations : ArrayList<Target>
                    ) : View.OnTouchListener {
    private var xDelta = 0f
    private var yDelta = 0f
    private var xRightDelta = 0f
    private var yBottomDelta = 0f

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val x = motionEvent.rawX
        val y = motionEvent.rawY
        val tolerance: Double = sqrt(pow(view.getWidth().toDouble(), 2.0) + pow(view.getHeight().toDouble(),
            2.0
        )) / 10
        val piece = view as PuzzlePiece
        if (!piece.canMove) {
            return true
        }
        val lParams = view.getLayoutParams() as RelativeLayout.LayoutParams
        when (motionEvent.action ) {
            MotionEvent.ACTION_DOWN -> {
                xDelta = x - lParams.leftMargin
                yDelta = y - lParams.topMargin
                piece.bringToFront()
            }
            MotionEvent.ACTION_MOVE -> {
                if((x - xDelta).toInt() > 0 && (y - yDelta).toInt() > 0 &&  x-xDelta+ view.width < screenWidth &&  y - yDelta + view.height < screenHeight){
                    lParams.leftMargin = (x - xDelta).toInt()
                    lParams.topMargin = (y - yDelta).toInt()
                    view.setLayoutParams(lParams)
//                    Log.e(">>>>>>>>>>>>", "$x $y $xRightDelta $yBottomDelta $screenWidth $screenHeight")
                }
            }
            MotionEvent.ACTION_UP -> {
                if(lParams.topMargin > piece.deviderLocationY ){
                    findAndReleaseCurrentTarget(piece.currentTarget)
                    piece.currentTarget = null
                    goBackToOrigin(lParams, piece)
                }else{
                    var minDistance = Int.MAX_VALUE
                    var tempTarget = Target(0,0)
                    targetLocations.forEach {
                        val dist = sqrt(pow((it.x-lParams.leftMargin).toDouble(), 2.0) + pow((it.y-lParams.topMargin).toDouble(), 2.0)).toInt()
                        if(dist < minDistance){
                            minDistance = dist
                            tempTarget = it
                        }
                    }
                    if(tempTarget.isOccupied){
                        goBackToOrigin(lParams, piece)
                    }else{
                        findAndReleaseCurrentTarget(piece.currentTarget)
                        piece.currentTarget = tempTarget
                        piece.currentTarget?.isOccupied = true
                        lParams.leftMargin = piece.currentTarget?.x ?: lParams.leftMargin
                        lParams.topMargin = piece.currentTarget?.y ?: lParams.topMargin
                        piece.layoutParams = lParams
                    }
//                    sendViewToBack(piece)
                }
                Log.e("CHOSEN VALUE CHOSEN", "${piece.deviderLocationY}")

//
//                val xDiff: Int = abs(piece.xCoord - lParams.leftMargin)
//                val yDiff: Int = abs(piece.yCoord - lParams.topMargin)
//                if (xDiff <= tolerance && yDiff <= tolerance) {
//                    lParams.leftMargin = piece.xCoord
//                    lParams.topMargin = piece.yCoord
//                    piece.layoutParams = lParams
//                    piece.canMove = false
//                    sendViewToBack(piece)
//                }
            }
        }
        return true
    }

    fun sendViewToBack(child: View) {
        val parent = child.getParent() as ViewGroup
        if (null != parent) {
            parent.removeView(child)
            parent.addView(child, 0)
        }
    }

    private fun goBackToOrigin(lParams: RelativeLayout.LayoutParams, piece: PuzzlePiece){
        if(piece.currentTarget != null){
            lParams.leftMargin = piece.currentTarget?.x ?: piece.xOriginalCoord
            lParams.topMargin = piece.currentTarget?.y ?: piece.yOriginalCoord
        }else{
            lParams.leftMargin = piece.xOriginalCoord
            lParams.topMargin = piece.yOriginalCoord
        }
        piece.layoutParams = lParams
    }

    fun findAndReleaseCurrentTarget(currentTarget: Target?){
        targetLocations.forEach {
            if(currentTarget?.id == it.id){
                it.isOccupied = false
            }
        }
    }
}

