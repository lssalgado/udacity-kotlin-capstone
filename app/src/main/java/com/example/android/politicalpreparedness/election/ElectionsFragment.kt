package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import timber.log.Timber

class ElectionsFragment: Fragment() {

    //TODO: Declare ViewModel

    private lateinit var viewModel: ElectionsViewModel
    private lateinit var binding: FragmentElectionBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val viewModelFactory = ElectionsViewModelFactory(requireActivity().application)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ElectionsViewModel::class.java)

        viewModel.elections.observe(viewLifecycleOwner, Observer {
            it.forEach { election ->
                Timber.e("Election = $election")
            }
        })
        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters
        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}