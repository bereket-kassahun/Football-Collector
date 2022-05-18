package com.collect22allfootball.ui.endgame

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.collect22allfootball.R

class EndGameFragment : Fragment() {

    companion object {
        fun newInstance() = EndGameFragment()
    }

    private lateinit var viewModel: EndGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[EndGameViewModel::class.java]
        return inflater.inflate(R.layout.end_game_fragment, container, false)
    }



}