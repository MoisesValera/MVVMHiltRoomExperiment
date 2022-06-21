package com.mevalera.mvvmhiltroomexperiment.presentation.restaurants

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevalera.mvvmhiltroomexperiment.data.model.Conference
import com.mevalera.mvvmhiltroomexperiment.databinding.ItemRestaurantBinding

class RestaurantAdapter :
    ListAdapter<Conference, RestaurantAdapter.RestaurantViewHolder>(RestaurantComparator()) {

    lateinit var itemClickListener: ClickEvent

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding =
            ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    fun submitAndUpdateList(list: List<Conference>) {
        submitList(null)
        submitList(list.toMutableList())
        notifyDataSetChanged()
    }

    inner class RestaurantViewHolder(private val binding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Conference) {
            binding.apply {
                conference = item
                clickListener = itemClickListener
                executePendingBindings()
            }
        }

    }

    class RestaurantComparator : DiffUtil.ItemCallback<Conference>() {
        override fun areItemsTheSame(oldItem: Conference, newItem: Conference) =
            oldItem._id == newItem._id

        override fun areContentsTheSame(oldItem: Conference, newItem: Conference) =
            oldItem == newItem
    }
}
