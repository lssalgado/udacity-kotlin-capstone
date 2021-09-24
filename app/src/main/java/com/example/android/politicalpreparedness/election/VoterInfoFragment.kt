package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.network.Result

class VoterInfoFragment : Fragment() {

    private lateinit var viewModel: VoterInfoViewModel
    private lateinit var binding: FragmentVoterInfoBinding
    private lateinit var toast: Toast

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val arguments = VoterInfoFragmentArgs.fromBundle(arguments!!)

        val viewModelFactory = VoterInfoViewModelFactory(requireActivity().application, arguments.argElectionId, arguments.argDivision)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.urlToLoad.observe(viewLifecycleOwner, Observer { url ->
            url?.let {
                startUrlIntent(it)
                viewModel.onUrlIntentStarted()
            }
        })

        viewModel.toastText.observe(viewLifecycleOwner, Observer { id ->
            id?.let {
                showToast(it)
                viewModel.onToastShown()
            }
        })

        viewModel.result.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                when (it) {
                    is Result.Error -> {
                        showToast(it.msg)
                        viewModel.onResultHandled()
                    }
                    is Result.HttpError -> {
                        showHttpErrorToast(it.code)
                        viewModel.onResultHandled()
                    }
                    is Result.Success -> {
                        viewModel.onResultHandled()
                    }
                }
            }
        })

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

    private fun showHttpErrorToast(code: Int) {
        if (::toast.isInitialized) {
            // Cancels the current toast to avoid queueing multiple toasts
            toast.cancel()
        }
        val string = getString(R.string.could_not_fetch_voter_info, code)
        toast = Toast.makeText(context, string, Toast.LENGTH_LONG)
        toast.show()
    }

    //TODO: Create method to load URL intents
    private fun startUrlIntent(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    private fun showToast(id: Int) {
        if (::toast.isInitialized) {
            // Cancels the current toast to avoid queueing multiple toasts
            toast.cancel()
        }
        toast = Toast.makeText(context, id, Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun showToast(msg: String) {
        if (::toast.isInitialized) {
            // Cancels the current toast to avoid queueing multiple toasts
            toast.cancel()
        }
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        toast.show()
    }

}