package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import timber.log.Timber

class ElectionsFragment: Fragment() {

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

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters
        val listener = ElectionListener {
            Timber.e("TODO implement ElectionListener")
        }
        val currentElectionsAdapter = ElectionListAdapter(listener)
        binding.upcomingElections.adapter = currentElectionsAdapter
        viewModel.currentElections.observe(viewLifecycleOwner, Observer { elections ->
            elections?.let {
                Timber.e("CurrentElections = ${it.joinToString("\n")}")
                currentElectionsAdapter.submitList(it)
            }
        })

        val savedElectionsAdapter = ElectionListAdapter(listener)
        binding.savedElections.adapter = savedElectionsAdapter
        viewModel.savedElections.observe(viewLifecycleOwner, Observer { elections ->
            elections?.let {
                Timber.e("SavedElections = ${it.joinToString("\n")}")
                savedElectionsAdapter.submitList(it)
            }
        })


        //TODO: Populate recycler adapters
        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}