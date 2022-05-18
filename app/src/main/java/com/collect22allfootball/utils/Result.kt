package com.collect22allfootball.utils

import android.graphics.Path
import com.collect22allfootball.PuzzlePiece

data class Result(
    val pieces: ArrayList<PuzzlePiece>?,
    val targetLocations: ArrayList<Target>,
    val overlayPath: Path
)
