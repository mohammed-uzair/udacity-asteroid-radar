package com.udacity.asteroidradar.screens.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data_source.database.AsteroidsDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.view_models.MainViewModel
import com.udacity.asteroidradar.view_models.view_models_factory.MainViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val adapter by lazy {
        AsteroidsAdapter()
    }

    private val viewModel: MainViewModel by lazy {
        val asteroidDao = AsteroidsDatabase.getAppDatabase(requireContext()).asteroidDao
        val repository = AsteroidRepository(asteroidDao)
        val viewModelFactory = MainViewModelFactory(repository, requireActivity().application)
        ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_all_menu -> {
            }
            R.id.show_rent_menu -> {
            }
            R.id.show_buy_menu -> {
            }
        }
        return true
    }

    private fun moveToAsteroidDetailScreen() {
//        val direction = MainFragmentDirections.actionShowDetail(Asteroid())
//        findNavController().navigate(direction)
    }
}
