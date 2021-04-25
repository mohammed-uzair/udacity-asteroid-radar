package com.udacity.asteroidradar.screens.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data_source.database.AsteroidsDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.view_models.MainViewModel
import com.udacity.asteroidradar.view_models.view_models_factory.MainViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by lazy {
        val database = AsteroidsDatabase.getAppDatabase(requireContext())
        val asteroidDao = database.asteroidDao
        val pictureOfDayDao = database.pictureOfDayDao
        val repository = AsteroidRepository(asteroidDao, pictureOfDayDao)

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
        val adapter = AsteroidsAdapter(OnClickListener {
            moveToAsteroidDetailScreen(it)
        })

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
            R.id.current_week_asteroids -> {
                // Get the asteroids from the server for this week
                viewModel.getAllAsteroids()
            }
            R.id.today_asteroids -> {
                // Get only today's asteroids
                viewModel.getTodayAsteroid()
            }
            R.id.saved_asteroids -> {
                // Get all the saved asteroids from the local cache
                viewModel.getAllAsteroids()
            }
        }
        return true
    }

    private fun moveToAsteroidDetailScreen(asteroid: Asteroid) {
        val direction = MainFragmentDirections.actionShowDetail(asteroid)
        findNavController().navigate(direction)
    }
}
