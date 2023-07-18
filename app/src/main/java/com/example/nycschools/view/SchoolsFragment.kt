package com.example.nycschools.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nycschools.R
import com.example.nycschools.databinding.FragmentSchoolsBinding
import com.example.nycschools.model.data.SchoolDetailData
import com.example.nycschools.model.repo.SortOrder
import com.example.nycschools.utils.onQueryTextChanged
import com.example.nycschools.view.adapters.SchoolsAdapter
import com.example.nycschools.viewmodel.SchoolViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.update

@AndroidEntryPoint
class SchoolsFragment: Fragment(R.layout.fragment_schools), SchoolsAdapter.OnSchoolClickListener {
    private val viewModel: SchoolViewModel by viewModels()

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
            schoolsAdapter.submitList(it)
        }

        menuHost.addMenuProvider(object: MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_fragment_schools, menu)
                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView

                searchView.onQueryTextChanged {changedText ->
                    viewModel.searchQuery.value = changedText
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId){
                    R.id.action_sort_by_SAT_DESC -> {
                        viewModel.onSortOrderSelected(SortOrder.BY_DESC)
                        return true
                    }
                    R.id.action_sort_by_SAT_ASC -> {
                        viewModel.onSortOrderSelected(SortOrder.BY_ASC)
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onSchoolSelected(schoolDetailData: SchoolDetailData) {

        // Passing data between fragments using safe nav args.
        val action = SchoolsFragmentDirections.actionSchoolsFragmentToSchoolDetailsFragment(schoolDetailData)
        view?.findNavController()?.navigate(action)
    }
}