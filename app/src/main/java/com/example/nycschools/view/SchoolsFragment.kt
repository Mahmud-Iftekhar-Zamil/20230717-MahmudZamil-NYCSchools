package com.example.nycschools.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nycschools.R
import com.example.nycschools.databinding.FragmentSchoolsBinding
import com.example.nycschools.model.data.SchoolDetailData
import com.example.nycschools.model.repo.SortOrder
import com.example.nycschools.utils.Events
import com.example.nycschools.utils.NetworkConnectivityStatus
import com.example.nycschools.utils.onQueryTextChanged
import com.example.nycschools.view.adapters.SchoolsAdapter
import com.example.nycschools.viewmodel.SchoolViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SchoolsFragment : Fragment(R.layout.fragment_schools), SchoolsAdapter.OnSchoolClickListener {
    private val viewModel: SchoolViewModel by viewModels()
    private lateinit var connectivityStatus: NetworkConnectivityStatus

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        val binding = FragmentSchoolsBinding.bind(view)
        val schoolsAdapter = SchoolsAdapter(this)

        binding.recyclerViewSchools.apply {
            adapter = schoolsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.schools.observe(viewLifecycleOwner) {
            //binding.recyclerViewSchools.isVisible = true
            schoolsAdapter.submitList(it)
        }

        /*        viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.schools.collect {
                            binding.recyclerViewSchools.isVisible = true
                            schoolsAdapter.submitList(it)
                        }
                    }
                }*/

        connectivityStatus = NetworkConnectivityStatus(requireContext())

        connectivityStatus.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                binding.progressBar.isVisible = true
                binding.textViewError.isVisible = false
                binding.recyclerViewSchools.isVisible = false
                Log.d("TEST", "fetchData() is called from connectivityStatus.observe()")
                viewModel.fetchData()
            } else if (!isConnected) {
                Snackbar.make(view, "No Internet. Check your internet connection", Snackbar.LENGTH_LONG).apply {
                    this.setTextColor(Color.YELLOW)
                    show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.svmEvent.collect { event ->
                    when (event) {
                        is Events.Success -> {
                            binding.progressBar.isVisible = false
                            binding.textViewError.isVisible = false
                            binding.recyclerViewSchools.isVisible = true
                        }

                        is Events.Error -> {
                            binding.progressBar.isVisible = false
                            binding.recyclerViewSchools.isVisible = false
                            binding.textViewError.apply {
                                this.text = event.error.message.toString()
                                this.isVisible = true
                            }
                        }
                    }
                }
            }
        }

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_fragment_schools, menu)
                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView

                searchView.onQueryTextChanged { changedText ->
                    viewModel.onSearchQueryChanged(changedText)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_sort_by_SAT_DESC -> {
                        viewModel.onSortOrderSelected(SortOrder.BY_DESC)
                        return true
                    }
                    R.id.action_sort_by_SAT_ASC -> {
                        viewModel.onSortOrderSelected(SortOrder.BY_ASC)
                        return true
                    }
                    R.id.action_refresh -> {
                        if (connectivityStatus.isConnected) {
                            binding.progressBar.isVisible = true
                            binding.textViewError.isVisible = false
                            binding.recyclerViewSchools.isVisible = false
                            viewModel.fetchData()
                        }
                        return true
                    }
                    else -> {
                        return true
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onSchoolSelected(schoolDetailData: SchoolDetailData) {
        // Passing data between fragments using safe nav args.
        val action =
            SchoolsFragmentDirections.actionSchoolsFragmentToSchoolDetailsFragment(schoolDetailData)
        view?.findNavController()?.navigate(action)
    }
}