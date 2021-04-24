package com.udacity.asteroidradar.screens.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding
import com.udacity.asteroidradar.models.Asteroid

class AsteroidsAdapter() :
    ListAdapter<Asteroid, AsteroidsAdapter.ViewHolder>(RouteDiffCallback) {
    private lateinit var binding: ItemAsteroidBinding

    inner class ViewHolder(private val asteroidBinding: ItemAsteroidBinding) :
        RecyclerView.ViewHolder(asteroidBinding.root) {
        init {
            binding = asteroidBinding

            asteroidBinding.root.setOnClickListener {
                val direction =
                    MainFragmentDirections.actionShowDetail(getItem(absoluteAdapterPosition))
                it.findNavController().navigate(direction)
            }
        }

        fun bindData(asteroid: Asteroid?) {
            asteroidBinding.asteroid = asteroid
        }
    }

    companion object RouteDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_asteroid, parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindData(getItem(holder.layoutPosition))
}