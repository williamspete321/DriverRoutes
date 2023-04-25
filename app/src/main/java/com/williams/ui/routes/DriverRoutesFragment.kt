package com.williams.ui.routes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.williams.R
import com.williams.data.Route
import com.williams.databinding.FragmentDriverRoutesBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import com.williams.ui.invisible
import com.williams.ui.toast
import com.williams.ui.visible
import com.williams.ui.routes.RoutesUiState.Loading
import com.williams.ui.routes.RoutesUiState.Success
import com.williams.ui.routes.RoutesUiState.Exception


class DriverRoutesFragment : Fragment() {

    private var _binding: FragmentDriverRoutesBinding? = null
    private val binding get() = _binding!!

    val viewModel: DriverRoutesViewModel by viewModel {
        parametersOf(
            arguments?.getInt(DRIVER_ID_KEY)
        )
    }

    private lateinit var adapter: RoutesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDriverRoutesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupAdapter()
        collectUiState()
    }

    private fun setupToolbar() {
        binding.routesTb.subtitle = arguments?.getString(DRIVER_NAME_KEY)
        binding.routesTb.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupAdapter() {
        adapter = RoutesAdapter { route: Route -> adapterOnClick(route) }
        binding.routesRv.adapter = adapter
    }

    private fun adapterOnClick(route: Route) {
        context?.toast(getString(R.string.route_message, route.id))
    }

    private fun collectUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is Exception -> {
                        binding.routesPb.invisible()
                        context?.toast(getString(R.string.error_message))
                    }
                    is Loading -> {
                        if (state.loading) {
                            binding.routesPb.visible()
                        }
                    }
                    is Success -> {
                        if (state.drivers.isNotEmpty()) {
                            binding.routesPb.invisible()
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

    companion object {
        const val DRIVER_ID_KEY = "driverId"
        const val DRIVER_NAME_KEY = "driverName"
    }
}