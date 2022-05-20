package com.collect22allfootball.ui.endgame

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.collect22allfootball.MainActivity
import com.collect22allfootball.R
import com.collect22allfootball.preference.PreferenceManager
import java.io.InputStream


private const val ARG_PARAM1 = "img_name"
class EndGameFragment : Fragment() {

    companion object {
        fun newInstance() = EndGameFragment()
    }

    private lateinit var viewModel: EndGameViewModel

    private var param1: Int = 1
    lateinit var imageView: ImageView
    lateinit var menu: Button
    lateinit var next: Button
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this)[EndGameViewModel::class.java]
        val root = inflater.inflate(R.layout.end_game_fragment, container, false)
        imageView = root.findViewById(R.id.img)
        menu = root.findViewById(R.id.menu)
        next = root.findViewById(R.id.next)

        //setting image
        val ims: InputStream = requireContext().assets.open("${param1}.png")
        val d = Drawable.createFromStream(ims, null)
        imageView.setImageDrawable(d)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    findNavController().popBackStack(R.id.nav_main_menu, false)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
        if(preferenceManager.getCurrentLevel() < 12){
            preferenceManager.setCurrentLevel(preferenceManager.getCurrentLevel()+1)
        }
        menu.setOnClickListener {
            findNavController().popBackStack(R.id.nav_main_menu, false)
        }

        next.setOnClickListener {

            findNavController().popBackStack(R.id.nav_game_play, false)
        }
    }


}