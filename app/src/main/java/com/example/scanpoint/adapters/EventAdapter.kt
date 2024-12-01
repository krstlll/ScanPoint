package com.example.scanpoint.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scanpoint.activities.EventDetailsActivity
import com.example.scanpoint.databinding.EachEventBinding
import com.example.scanpoint.models.EventModel

class EventAdapter(val context : Context, val eventList: ArrayList<EventModel>?): RecyclerView.Adapter<EventAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.ViewHolder {
        return ViewHolder(EachEventBinding.inflate(LayoutInflater.from(parent.context), parent, false), context)
    }

    override fun getItemCount(): Int = this.eventList?.size!!

    override fun onBindViewHolder(holder: EventAdapter.ViewHolder, position: Int) {
        eventList?.get(position).let {
            if (it != null) {
                holder.bind(it, position)
            }
        }
    }

    class ViewHolder(val binding: EachEventBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventModel , position : Int) {
            binding.tvPreviewMessage.text = event.eventName
            binding.tvTimestamp.text = event.eventDate

            binding.clEach.setOnClickListener {
                val intent = Intent(context, EventDetailsActivity::class.java)
                intent.putExtra("event_uid", event.uid)
                context.startActivity(intent)
            }
        }
    }

    fun updateEvents(newEvents: List<EventModel>) {
        eventList?.clear()
        eventList?.addAll(newEvents)
        notifyDataSetChanged()
    }
}