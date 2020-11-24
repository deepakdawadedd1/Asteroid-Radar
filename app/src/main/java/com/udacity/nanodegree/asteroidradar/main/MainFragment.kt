package com.udacity.nanodegree.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.nanodegree.asteroidradar.R
import com.udacity.nanodegree.asteroidradar.database.entities.Asteroid
import com.udacity.nanodegree.asteroidradar.databinding.FragmentMainBinding
import com.udacity.nanodegree.asteroidradar.databinding.ListItemAsteroidBinding

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainViewModelFactory = MainViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        val adapter = AsteroidAdapter {
            viewModel.navigateToDetails(it)
        }
        binding.asteroidRecycler.adapter = adapter
        viewModel.feeds.observe(viewLifecycleOwner) {
            viewModel.progress(it.isNullOrEmpty())
            adapter.submitList(it)
        }

        viewModel.navigator.observe(viewLifecycleOwner) { asteroid ->
            asteroid?.let {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.navigationDone()
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}

class AsteroidAdapter(private val itemClickListener: OnItemClickListener) :
    ListAdapter<Asteroid, AsteroidAdapter.ViewHolder>(AsteroidDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.getInstance(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, itemClickListener)
    }

    class ViewHolder private constructor(private val binding: ListItemAsteroidBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid, listener: OnItemClickListener) {
            binding.asteroid = asteroid
            binding.listener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun getInstance(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return ViewHolder(ListItemAsteroidBinding.inflate(inflater, parent, false))
            }
        }
    }

    object AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }

    fun interface OnItemClickListener {
        fun onItemClick(asteroid: Asteroid)
    }
}