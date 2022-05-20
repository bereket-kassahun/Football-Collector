package com.collect22allfootball.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.SurfaceView
import android.widget.ImageView
import android.widget.RelativeLayout
import com.collect22allfootball.PuzzlePiece
import kotlin.math.abs
import kotlin.math.roundToInt


val UTIL_IMAGE_CROP_ERROR = "IMAGE CROPPING"
private fun getBitmapPositionInsideImageView(imageView: ImageView?): IntArray? {
    val ret = IntArray(4)
    if (imageView?.drawable == null) return ret

    // Get image dimensions
    // Get image matrix values and place them in an array
    val f = FloatArray(9)
    imageView.getImageMatrix().getValues(f)

    // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
    val scaleX = f[Matrix.MSCALE_X]
    val scaleY = f[Matrix.MSCALE_Y]

    // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
    val d: Drawable = imageView.getDrawable()
    val origW = d.intrinsicWidth
    val origH = d.intrinsicHeight

    // Calculate the actual dimensions
    val actW = (origW * scaleX).roundToInt()
    val actH = (origH * scaleY).roundToInt()
    ret[2] = actW
    ret[3] = actH

    // Get image position
    // We assume that the image is centered into ImageView
    val imgViewW: Int = imageView.getWidth()
    val imgViewH: Int = imageView.getHeight()
    val top = (imgViewH - actH) / 2
    val left = (imgViewW - actW) / 2
    ret[0] = left
    ret[1] = top
    return ret
}

fun splitImage(imageView: ImageView?, context: Context): Result {

    val pieces: ArrayList<PuzzlePiece> = ArrayList()
    val targetLocations = ArrayList<Target>()
    val overlayPath = Path()

    val rows = 5
    val cols = 3
//    val imageView: ImageView = findViewById(R.id.imageView)

    // Get the scaled bitmap of the source image
    val drawable = imageView?.drawable as BitmapDrawable
    val bitmap = drawable.bitmap
    val dimensions = getBitmapPositionInsideImageView(imageView)
    val scaledBitmapLeft = dimensions!![0]
    val scaledBitmapTop = dimensions[1]
    val scaledBitmapWidth = dimensions[2]
    val scaledBitmapHeight = dimensions[3]

    val croppedImageWidth: Int = scaledBitmapWidth - 2 * abs(scaledBitmapLeft)
    val croppedImageHeight: Int = scaledBitmapHeight - 2 * abs(scaledBitmapTop)

    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true)
    val croppedBitmap = Bitmap.createBitmap(
        scaledBitmap,
        abs(scaledBitmapLeft),
        abs(scaledBitmapTop),
        croppedImageWidth,
        croppedImageHeight
    )

    // Calculate the optimal width
    val pieceWidth = calculateOptimalWidth(croppedImageWidth, cols)


    var pieceHeight = croppedImageHeight / rows

    //correcting the half height that  remain
    val difference = pieceHeight/1.9f/rows
    pieceHeight -= difference.toInt()

    // Create each bitmap piece and add it to the resulting array
    var xCoord = 0
    var col = 0
    var id = 1
    while (col < cols){
        var yCoord = 0
        val verticalOffset = if(col % 2 == 1) pieceHeight/2-1 else 0
        var row = 0
        while(row < rows){
            val piece = PuzzlePiece(context)

            Log.e(UTIL_IMAGE_CROP_ERROR,"$yCoord $pieceHeight ${croppedBitmap.height}" )
            val pieceBitmap = Bitmap.createBitmap(croppedBitmap,
                xCoord, yCoord + verticalOffset,
                if(xCoord + pieceWidth < croppedImageWidth) pieceWidth else croppedImageWidth-xCoord,
                if(yCoord + pieceHeight < croppedImageHeight) pieceHeight else croppedImageHeight-yCoord )
            piece.xCoord = xCoord  + imageView.left
            piece.yCoord = yCoord + verticalOffset + imageView.top
            piece.pieceWidth = pieceWidth
            piece.pieceHeight = pieceHeight

            piece._id = id
            //setting target locations
            targetLocations.add(Target(piece.xCoord, piece.yCoord, id = id))
            id++

            //for some reason the piece keeps expanding so this is to force it to keep specific width and height
            val parms: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(pieceWidth, pieceHeight)
            piece.layoutParams = parms

            shapePuzzlePiece(pieceBitmap, pieceWidth, pieceHeight,  xCoord, yCoord + verticalOffset, overlayPath)
            piece.setImageBitmap(pieceBitmap)
            pieces.add(piece)

            yCoord += pieceHeight
            row++
        }
        xCoord += (pieceWidth*0.7).toInt()-1
        col++
    }

    // drawing on surface view


    return Result(pieces, targetLocations, overlayPath)
}


fun shapePuzzlePiece(bitmap: Bitmap, width: Int, height: Int,  x: Int, y: Int, overlayPath: Path){

    val canvas = Canvas(bitmap)
    val path = Path()

    val leftCorner = Pair(0f, height/2f)
    val topLeftCorner = Pair(width*0.3f, 0f)
    val topRightCorner = Pair(width*0.7f, 0f)
    val rightCorner = Pair(width.toFloat(), height/2f)
    val bottomRightCorner = Pair(width*0.7f, height.toFloat())
    val bottomLeftCorner = Pair(width*0.3f, height.toFloat())

    path.moveTo(leftCorner.first, leftCorner.second)

    path.lineTo(topLeftCorner.first, topLeftCorner.second)
    path.lineTo(topRightCorner.first, topRightCorner.second)
    path.lineTo(rightCorner.first, rightCorner.second)
    path.lineTo(bottomRightCorner.first, bottomRightCorner.second)
    path.lineTo(bottomLeftCorner.first, bottomLeftCorner.second)
    path.lineTo(leftCorner.first, leftCorner.second)

    path.close()


    // draw a black border
    var border = Paint()
    border.color = -0x7f000001
    border.style = Paint.Style.STROKE
    border.strokeWidth = 1.0f
    canvas.drawPath(path, border)
    // mask the piece
    val paint = Paint()
    paint.color = Color.argb(0,0,0,0)
    paint.style = Paint.Style.FILL
    path.fillType = Path.FillType.INVERSE_EVEN_ODD
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawPath(path, paint)



    // constructing path for overlay
    val overlayLeftCorner = Pair(x+0f, y+height/2f)
    val overlayTopLeftCorner = Pair(x+width*0.3f, y+0f)
    val overlayTopRightCorner = Pair(x+width*0.7f, y+0f)
    val overlayRightCorner = Pair(x+width.toFloat(), y+height/2f)
    val overlayBottomRightCorner = Pair(x+width*0.7f, y+height.toFloat())
    val overlayBottomLeftCorner = Pair(x+width*0.3f, y+height.toFloat())

    overlayPath.moveTo(overlayLeftCorner.first, overlayLeftCorner.second)
    overlayPath.lineTo(overlayTopLeftCorner.first, overlayTopLeftCorner.second)
    overlayPath.lineTo(overlayTopRightCorner.first, overlayTopRightCorner.second)
    overlayPath.lineTo(overlayRightCorner.first, overlayRightCorner.second)
    overlayPath.lineTo(overlayBottomRightCorner.first, overlayBottomRightCorner.second)
    overlayPath.lineTo(overlayBottomLeftCorner.first, overlayBottomLeftCorner.second)
    overlayPath.lineTo(overlayLeftCorner.first, overlayLeftCorner.second)
//    overlayPath.close()
}

fun calculateOptimalWidth(availableWidth: Int, cols: Int): Int{
    // Calculate the optimal width
    var pieceWidth = availableWidth / cols
    var max_width = pieceWidth
    val ratio = 0.7 * (cols-1)
    while(max_width < availableWidth){
        pieceWidth = max_width
        if((max_width * ratio + max_width) < availableWidth){
            max_width++
        }else{
            break
        }
    }
    return pieceWidth
}

fun calculateWidthRatio(availableWidth: Int, cols: Int, pieceWidth: Int): Float{
    var ratio = 0f
    var tempWidth = pieceWidth
    while(tempWidth < availableWidth){
        ratio += 0.1f
        tempWidth = (ratio*(cols-1)*pieceWidth + pieceWidth).toInt()
    }
    return ratio
}