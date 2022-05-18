package com.collect22allfootball.ui.gameplay

import android.graphics.Path
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.collect22allfootball.PuzzlePiece
import com.collect22allfootball.utils.Target
import com.collect22allfootball.utils.splitImage

class GamePlayViewModel : ViewModel() {
    private val _pieces = MutableLiveData<ArrayList<PuzzlePiece>?>()
    private val _targetLocations = MutableLiveData<ArrayList<Target>?>()
    private val _overlayPath = MutableLiveData<Path?>()

    val pieces: LiveData<ArrayList<PuzzlePiece>?> = _pieces
    val targetLocations: LiveData<ArrayList<Target>?> = _targetLocations
    val overlayPath: LiveData<Path?> = _overlayPath

    init {

    }

    fun setPieces(pieces: ArrayList<PuzzlePiece>?){
        _pieces.value = pieces
    }
    fun setTargetLocations(targetLocations: ArrayList<Target>){
        _targetLocations.value = targetLocations
    }
    fun setOverlayPath(path: Path){
        _overlayPath.value = path
    }
}