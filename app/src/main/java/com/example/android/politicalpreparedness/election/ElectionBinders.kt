package com.example.android.politicalpreparedness.election

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.State

@BindingAdapter("saveButtonText")
fun Button.setText(election: Election?) {
    if (election != null && election.saved) {
        setText(R.string.unfollow_election)
    } else {
        setText(R.string.follow_election)
    }
}

@BindingAdapter("stateLocationText")
fun TextView.setLocationTextAndIcon(election: Election?) {
    setTextAndIcon(election, R.string.voting_locations, R.drawable.ic_where_to_vote, R.string.missing_voting_locations)
}

@BindingAdapter("stateBallotText")
fun TextView.setBallotTextAndIcon(election: Election?) {
    setTextAndIcon(election, R.string.ballot_informations, R.drawable.ic_how_to_vote, R.string.missing_ballot_informations)
}

fun TextView.setTextAndIcon(election: Election?, stringId: Int, drawableId: Int, missingStringId: Int) {
    if (election != null) {
        setText(stringId)
        setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(drawableId), null, null, null)
    } else {
        setText(missingStringId)
        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
    }
}

@BindingAdapter("addressText")
fun TextView.setAddressText(state: State?) {
    if (state != null && state.electionAdministrationBody.correspondenceAddress != null) {
        text = state.electionAdministrationBody.correspondenceAddress.toFormattedString()
    } else {
        setText(R.string.missing_address)
    }
}