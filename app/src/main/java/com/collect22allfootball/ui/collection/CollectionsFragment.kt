package com.collect22allfootball.ui.collection

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.GridView
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.collect22allfootball.MainActivity
import com.collect22allfootball.R
import com.collect22allfootball.preference.PreferenceManager


class CollectionsFragment : Fragment() {

    companion object {
        fun newInstance() = CollectionsFragment()
    }

    private lateinit var viewModel: CollectionsViewModel
    private lateinit var preferences: PreferenceManager
    private lateinit var gridView: GridView
    private lateinit var backButton: ImageButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[CollectionsViewModel::class.java]
        val root = inflater.inflate(R.layout.collections_fragment, container, false)
        gridView = root.findViewById(R.id.gridView)
        backButton = root.findViewById(R.id.back_button)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = PreferenceManager(requireContext())

        gridView.adapter = GridViewAdapter(requireContext(), preferences.getCurrentLevel())

        gridView.onItemClickListener = OnItemClickListener { _, _, position, id ->
            if(preferences.getCurrentLevel()+1 > id){
                val navigator = (requireActivity() as MainActivity).navController
                val bundle = Bundle()
                bundle.putInt("img_name", (id+1).toInt())
                navigator.navigate(R.id.nav_image_detail, bundle)
            }
        }

        backButton.setOnClickListener {
            (requireActivity() as MainActivity).navController.navigate(R.id.nav_main_menu)
        }
    }
}