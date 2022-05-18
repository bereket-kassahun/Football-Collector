package com.collect22allfootball.ui.gameplay

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.lifecycle.lifecycleScope
import com.collect22allfootball.R
import com.collect22allfootball.TouchListener
import com.collect22allfootball.utils.calculateWidthRatio
import com.collect22allfootball.utils.splitImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GamePlayFragment : Fragment() {

    companion object {
        fun newInstance() = GamePlayFragment()
    }

    private lateinit var viewModel: GamePlayViewModel

    private lateinit var relativeLayout: RelativeLayout
    private lateinit var imageView: ImageView
    private lateinit var overlay: SurfaceView
    private lateinit var devider: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(GamePlayViewModel::class.java)
        val root = inflater.inflate(R.layout.game_play_fragment, container, false)
        relativeLayout = root.findViewById(R.id.layout)
        imageView = root.findViewById(R.id.imageView)
        overlay = root.findViewById(R.id.overlay)
        devider = root.findViewById(R.id.devider)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        overlay.setZOrderOnTop(true)
        overlay.holder.setFormat(PixelFormat.TRANSPARENT)

        imageView.post(){
            init()
        }

        viewModel.overlayPath.observe(viewLifecycleOwner) {
            if(it != null)
                drawOnOverlay(it, overlay)
        }

    }


    fun init(){
        lifecycleScope.launch(Dispatchers.IO) {
            val result = splitImage(imageView, requireContext())
            val pieces = result.pieces
            val targetLocations = result.targetLocations
            val overlayPath = result.overlayPath
            pieces?.shuffle()
            withContext(Dispatchers.Main) {
                viewModel.setPieces(pieces)
                viewModel.setTargetLocations(targetLocations)
                viewModel.setOverlayPath(overlayPath)

                val piecePerPlace = pieces?.size?.div(5) ?: 2
                val pieceWidth = pieces?.get(0)?.pieceWidth ?: 0
                val pieceHeight = pieces?.get(0)?.pieceHeight ?: 0
                var xStartPlace = imageView.left + 50
                var yStartPlace = devider.top + 40
                val widthRatio = calculateWidthRatio(imageView.right - pieceWidth, 5, pieceWidth)
                var tempPlace = 0


                val touchListener =
                    TouchListener(relativeLayout.width, relativeLayout.height, targetLocations)
                pieces?.forEach { piece ->
                    tempPlace++
                    piece.setOnTouchListener(touchListener)

                    //setting starting place for puzzle pieces
                    val param = piece.layoutParams as RelativeLayout.LayoutParams
                    param.leftMargin = xStartPlace
                    param.topMargin = yStartPlace


                    piece.xOriginalCoord = xStartPlace
                    piece.yOriginalCoord = yStartPlace
                    piece.deviderLocationY = devider.top
                    relativeLayout.addView(piece, param)
                    if (tempPlace >= piecePerPlace) {
                        xStartPlace += (pieceWidth * widthRatio).toInt()
                        tempPlace = 0
                    }
                }
            }

        }
    }
    fun drawOnOverlay(path: Path, overlay: SurfaceView) {
        val overlayCanvas = overlay.holder.lockCanvas()
        val overlayPaint = Paint()
        overlayPaint.color = Color.DKGRAY
        overlayPaint.style = Paint.Style.STROKE
        overlayPaint.strokeWidth = 2.0f
        overlayCanvas.drawPath(path, overlayPaint)
        overlay.holder.unlockCanvasAndPost(overlayCanvas)
    }
}