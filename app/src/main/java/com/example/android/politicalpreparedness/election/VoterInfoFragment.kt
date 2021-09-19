package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private lateinit var viewModel: VoterInfoViewModel
    private lateinit var binding: FragmentVoterInfoBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val arguments = VoterInfoFragmentArgs.fromBundle(arguments!!)

        val viewModelFactory = VoterInfoViewModelFactory(requireActivity().application, arguments.argElectionId, arguments.argDivision)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)



        //TODO: Add binding values

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */


        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks
        return binding.root
    }

    //TODO: Create method to load URL intents

}