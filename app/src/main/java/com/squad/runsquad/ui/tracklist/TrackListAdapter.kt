package com.squad.runsquad.ui.tracklist

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squad.runsquad.R
import com.squad.runsquad.data.model.Track
import com.squad.runsquad.util.inflate
import com.squad.runsquad.util.round
import kotlinx.android.synthetic.main.item_track.view.*
import java.util.concurrent.TimeUnit

class TrackListAdapter: RecyclerView.Adapter<TrackListAdapter.TrackViewHolder>() {

    private var trackList = arrayListOf<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent.inflate(R.layout.item_track))

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bindData(trackList[position])
    }

    inner class TrackViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {

        fun bindData(track: Track) {

            with(itemView) {
                distanceValue.text = track.distanceTravelled.round().toString()
                timeValue.text = formatTime(track.timeElapsed)
                paceValue.text = formatPace(track.averagePace.round())
            }
        }
    }

    fun updateTrackList(newList: ArrayList<Track>?) {
        trackList = newList ?: arrayListOf()
        notifyDataSetChanged()
    }

    private fun formatTime(time: Long): String =
        String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(time),
            TimeUnit.MILLISECONDS.toSeconds(time) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)))

    private fun formatPace(pace: Float): String {
        val paceBeforeSeparator = pace.toInt()
        val paceAfterSeparator = (pace % 1) * 100
        return String.format("%2d:%02d", paceBeforeSeparator, paceAfterSeparator.toInt())
    }
}