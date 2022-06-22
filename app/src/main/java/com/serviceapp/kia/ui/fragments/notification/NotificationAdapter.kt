package com.serviceapp.kia.ui.fragments.notification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.MyVehiclesApi.*
import com.serviceapp.kia.data.network.responses.NotificationApi.*
import com.serviceapp.kia.data.network.responses.VehicleHistoryApi.*
import com.serviceapp.kia.databinding.ListNotificationBinding
import com.serviceapp.kia.utils.setDateMonthYear
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/*
 *Created by Adithya T Raj on 11-07-2021
*/

class NotificationAdapter(
    private val clickListener: (NotificationDataResponse) -> Unit
): RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    private val diffCallBack = object : DiffUtil.ItemCallback<NotificationDataResponse>() {
        override fun areItemsTheSame(
            oldItem: NotificationDataResponse,
            newItem: NotificationDataResponse
        ): Boolean {
            return oldItem.title == newItem.title
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: NotificationDataResponse,
            newItem: NotificationDataResponse
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallBack)

    fun submitList(list: List<NotificationDataResponse>) {
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
        private val binding: ListNotificationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NotificationDataResponse, clickListener: (NotificationDataResponse) -> Unit) {
            binding.listNotificationTitleTxt.text = item.title
            binding.listNotificationBodyTxt.text = item.message
            binding.listNotificationDateTxt.text = setDateMonthYear(item.created_at.toString())
            val bg = when (item.readed_status) {
                "1" -> R.drawable.dot_ash
                else -> R.drawable.dot
            }
            binding.view.background = ContextCompat.getDrawable(binding.root.context, bg)
            binding.listNotificationDeleteImg.setOnClickListener {
                clickListener(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListNotificationBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }


}