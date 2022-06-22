package com.serviceapp.kia.ui.fragments.my.vehicle

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.serviceapp.kia.data.network.responses.MyVehiclesApi.*
import com.serviceapp.kia.databinding.ListMyVehicleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/*
 *Created by Adithya T Raj on 02-07-2021
*/

class MyVehicleAdapter(
    private val clickListener: (MyVehiclesDataResponse) -> Unit
): RecyclerView.Adapter<MyVehicleAdapter.ViewHolder>() {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    private val diffCallBack = object : DiffUtil.ItemCallback<MyVehiclesDataResponse>() {
        override fun areItemsTheSame(
            oldItem: MyVehiclesDataResponse,
            newItem: MyVehiclesDataResponse
        ): Boolean {
            return oldItem.vehicle_id == newItem.vehicle_id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: MyVehiclesDataResponse,
            newItem: MyVehiclesDataResponse
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallBack)

    fun submitList(list: List<MyVehiclesDataResponse>) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                differ.submitList(list.toList())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position], clickListener)
    }

    override fun getItemCount() = differ.currentList.size

    class ViewHolder(
        private val binding: ListMyVehicleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyVehiclesDataResponse, clickListener: (MyVehiclesDataResponse) -> Unit) {
            binding.listMyVehicleNameTxt.text = item.vehicle_model
            binding.listMyVehicleChassisTxt.text = item.vehicle_chassis_no
            binding.listMyVehiclePlateTxt.text = item.vehicle_regno
            binding.listMyVehicleYearTxt.text = item.vehicle_reg_year
            binding.listMyVehicleDateTxt.text = item.due_date
            binding.listMyVehicleMilageTxt.text = item.due_mileage
            binding.vehicleDeleteImg.setOnClickListener {
                clickListener(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListMyVehicleBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }


}