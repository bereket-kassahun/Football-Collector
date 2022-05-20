package com.collect22allfootball.ui.mainmenu

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.collect22allfootball.MainActivity
import com.collect22allfootball.R

class MainMenuFragment : Fragment() {

    companion object {
        fun newInstance() = MainMenuFragment()
    }

    lateinit var start: Button
    lateinit var collections: Button

    private lateinit var viewModel: MainMenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[MainMenuViewModel::class.java]

        val root = inflater.inflate(R.layout.main_menu_fragment, container, false)
        start = root.findViewById(R.id.start)
        collections = root.findViewById(R.id.collection)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start.setOnClickListener {
            val navigation = (requireActivity() as MainActivity).navController
            navigation.navigate(R.id.nav_game_play)
        }

        collections.setOnClickListener {
            val navigation = (requireActivity() as MainActivity).navController
            navigation.navigate(R.id.nav_collections)
        }
    }
}