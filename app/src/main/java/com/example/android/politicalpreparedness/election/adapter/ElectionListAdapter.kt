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

class ElectionListAdapter(private val clickListener: ElectionListener): ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    //TODO: Bind ViewHolder

    //TODO: Add companion object to inflate ViewHolder (from)
}

//TODO: Create ElectionViewHolder
class ElectionViewHolder(val binding: ViewholderElectionBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Election) {
        //TODO fill this method
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

//TODO: Create ElectionDiffCallback
//TODO: Validate this class
class ElectionDiffCallback: DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }
}

//TODO: Create ElectionListener
//TODO: Validate this class
class ElectionListener(val listener: (election: Election) -> Unit) {
    fun onClick(election: Election) = listener(election)
}