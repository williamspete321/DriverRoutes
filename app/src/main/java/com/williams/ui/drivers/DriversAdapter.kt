package com.williams.ui.drivers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.williams.R
import com.williams.data.Driver
import com.williams.databinding.ListItemDriverBinding

class DriversAdapter(private val onClick: (Driver) -> Unit) :
    ListAdapter<Driver, DriversAdapter.DriversViewHolder>(DriversDiffCallback) {

    class DriversViewHolder(val binding: ListItemDriverBinding, val onClick: (Driver) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        private var currentDriver: Driver? = null

        init {
            itemView.setOnClickListener {
                currentDriver?.let {
                    onClick(it)
                }
            }
        }

        fun bind(driver: Driver) {
            currentDriver = driver

            val context = binding.root.context
            val textView = binding.driverNameTv

            textView.text = context.getString(
                R.string.driver_list_item_name_format,
                driver.lastName,
                driver.firstName
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriversViewHolder {
        val binding = ListItemDriverBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DriversViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: DriversViewHolder, position: Int) {
        val driver = getItem(position)
        holder.bind(driver)
    }
}

object DriversDiffCallback : DiffUtil.ItemCallback<Driver>() {
    override fun areItemsTheSame(oldItem: Driver, newItem: Driver): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Driver, newItem: Driver): Boolean {
        return oldItem.id == newItem.id
    }
}