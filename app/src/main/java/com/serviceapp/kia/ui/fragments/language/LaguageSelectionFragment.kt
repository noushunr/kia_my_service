package com.serviceapp.kia.ui.fragments.language

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.serviceapp.kia.R

class LaguageSelectionFragment : Fragment() {

    companion object {
        fun newInstance() = LaguageSelectionFragment()
    }

    private lateinit var viewModel: LaguageSelectionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LaguageSelectionViewModel::class.java)
        return inflater.inflate(R.layout.laguage_selection_fragment, container, false)
    }

}