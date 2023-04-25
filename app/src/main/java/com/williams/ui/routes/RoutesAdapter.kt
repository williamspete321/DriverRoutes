package com.williams.ui.routes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.williams.R
import com.williams.data.Route
import com.williams.databinding.ListItemRouteBinding

class RoutesAdapter(private val onClick: (Route) -> Unit) :
    ListAdapter<Route, RoutesAdapter.RoutesViewHolder>(RoutesDiffCallback) {

    class RoutesViewHolder(val binding: ListItemRouteBinding, val onClick: (Route) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        private var currentRoute: Route? = null

        init {
            itemView.setOnClickListener {
                currentRoute?.let {
                    onClick(it)
                }
            }
        }

        fun bind(route: Route) {
            currentRoute = route

            val context = binding.root.context
            val routeNameTv = binding.routeNameTv
            val routeTypeTv = binding.routeTypeTv

            routeNameTv.text = route.name
            routeTypeTv.text = context.getString(
                R.string.route_type_list_item,
                route.type
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutesViewHolder {
        val binding = ListItemRouteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RoutesViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: RoutesViewHolder, position: Int) {
        val route = getItem(position)
        holder.bind(route)
    }
}

object RoutesDiffCallback : DiffUtil.ItemCallback<Route>() {
    override fun areItemsTheSame(oldItem: Route, newItem: Route): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Route, newItem: Route): Boolean {
        return oldItem.id == newItem.id
    }
}