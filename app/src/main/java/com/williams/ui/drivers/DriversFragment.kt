package com.williams.ui.drivers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.williams.R
import com.williams.data.Driver
import com.williams.databinding.FragmentDriversBinding
import com.williams.ui.drivers.DriversFragmentDirections
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.williams.ui.invisible
import com.williams.ui.toast
import com.williams.ui.visible
import com.williams.ui.drivers.DriversUiState.Loading
import com.williams.ui.drivers.DriversUiState.Success
import com.williams.ui.drivers.DriversUiState.Exception

class DriversFragment : Fragment() {

    private var _binding: FragmentDriversBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DriversViewModel by viewModel<DriversViewModel>()

    private lateinit var adapter: DriversAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDriversBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBarMenu()
        setupAdapter()
        collectUiState()
    }

    private fun setupToolBarMenu() {
        val toolbar = binding.driversTb
        toolbar.inflateMenu(R.menu.menu_drivers)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sort_by_last_name -> viewModel.onSortByLastNameClicked()
            }
            true
        }
    }

    private fun setupAdapter() {
        adapter = DriversAdapter { driver: Driver -> adapterOnClick(driver) }
        binding.driversRv.adapter = adapter
    }

    private fun adapterOnClick(driver: Driver) {
        val directions = DriversFragmentDirections.actionFragmentDriversToFragmentDriverRoutes(
            driver.id,
            getString(R.string.driver_list_item_name_format, driver.lastName, driver.firstName)
        )
        findNavController().navigate(directions)
    }

    private fun collectUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is Exception -> {
                        binding.driversPb.invisible()
                        context?.toast(getString(R.string.error_message))
                    }
                    is Loading -> {
                        if (state.loading) {
                            binding.driversPb.visible()
                        }
                    }
                    is Success -> {
                        if (state.drivers.isNotEmpty()) {
                            binding.driversPb.invisible()
                            adapter.submitList(state.drivers)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}