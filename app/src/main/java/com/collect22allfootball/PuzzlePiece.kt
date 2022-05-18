package com.collect22allfootball

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.collect22allfootball.utils.Target
import java.util.ArrayList

data class PuzzlePiece(
    val cnt: Context,
    var xCoord: Int = 0,
    var yCoord: Int = 0,
    var xOriginalCoord:Int = 0,
    var yOriginalCoord:Int = 0,
    var deviderLocationY: Int = 0,
    var pieceWidth: Int = 0,
    var pieceHeight: Int = 0,
    var canMove: Boolean = true,
    var _id: Int = 0,
    var currentTarget: Target? = null
) : AppCompatImageView(cnt)