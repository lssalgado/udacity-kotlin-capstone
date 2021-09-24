package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.repository.ElectionRepository
import timber.log.Timber

class ElectionsFragment: Fragment() {

    private lateinit var viewModel: ElectionsViewModel
    private lateinit var binding: FragmentElectionBinding
    private lateinit var toast: Toast

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
        val listener = ElectionListener { election ->
            findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id, election.division))
        }
        val currentElectionsAdapter = ElectionListAdapter(listener)
        binding.upcomingElections.adapter = currentElectionsAdapter
        viewModel.currentElections.observe(viewLifecycleOwner, Observer { elections ->
            elections?.let {
                Timber.e("CurrentElections = ${it.joinToString("\n")}")
                if (it.isNotEmpty()) {
                    binding.upcomingLoading.visibility = View.INVISIBLE
                }
                currentElectionsAdapter.submitList(it)
            }
        })

        val savedElectionsAdapter = ElectionListAdapter(listener)
        binding.savedElections.adapter = savedElectionsAdapter
        viewModel.savedElections.observe(viewLifecycleOwner, Observer { elections ->
            elections?.let {
                Timber.e("SavedElections = ${it.joinToString("\n")}")
                binding.savedLoading.visibility = View.INVISIBLE
                savedElectionsAdapter.submitList(it)
            }
        })

        viewModel.result.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                when(it) {
                    is ElectionRepository.Result.Error -> {
                        showToast(it.msg)
                        viewModel.onResultHandled()
                    }
                    is ElectionRepository.Result.HttpError -> {
                        showHttpErrorToast(it.code)
                        viewModel.onResultHandled()
                    }
                    else -> {
                        viewModel.onResultHandled()
                    }
                }
            }
        })

        //TODO: Populate recycler adapters
        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

    private fun showToast(msg: String) {
        if (::toast.isInitialized) {
            // Cancels the current toast to avoid queueing multiple toasts
            toast.cancel()
        }
        toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
        toast.show()
    }

    private fun showHttpErrorToast(code: Int) {
        if (::toast.isInitialized) {
            // Cancels the current toast to avoid queueing multiple toasts
            toast.cancel()
        }
        val string = getString(R.string.could_not_fetch_elections, code)
        toast = Toast.makeText(context, string, Toast.LENGTH_LONG)
        toast.show()
    }
}