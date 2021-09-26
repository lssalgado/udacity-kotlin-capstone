package com.example.android.politicalpreparedness.election.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.ViewholderElectionBinding
import com.example.android.politicalpreparedness.databinding.ViewholderRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Channel
//import com.example.android.politicalpreparedness.databinding.ViewholderElectionBinding
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ElectionListAdapter(private val electionListener: ElectionListener): ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, electionListener)
    }

    override fun submitList(list: List<Election>?) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                super.submitList(list)
            }
        }
    }
}

class ElectionViewHolder(val binding: ViewholderElectionBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Election, electionListener: ElectionListener) {
        binding.election = item
        binding.electionListener = electionListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ElectionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binder = ViewholderElectionBinding.inflate(layoutInflater, parent, false)
            return ElectionViewHolder(binder)
        }
    }
}

class ElectionDiffCallback: DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }
}

class ElectionListener(val listener: (election: Election) -> Unit) {
    fun onClick(election: Election) = listener(election)
}