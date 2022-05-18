package com.collect22allfootball.ui.collection

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.collect22allfootball.R

class CollectionsFragment : Fragment() {

    companion object {
        fun newInstance() = CollectionsFragment()
    }

    private lateinit var viewModel: CollectionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this)[CollectionsViewModel::class.java]
        return inflater.inflate(R.layout.collections_fragment, container, false)
    }


}