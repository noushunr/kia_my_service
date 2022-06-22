package com.serviceapp.kia.ui.fragments.appointment.servicetype

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.serviceapp.kia.MainApplication
import com.serviceapp.kia.data.network.responses.ServiceTypeApi.ServiceTypeDataResponse
import com.serviceapp.kia.data.preferences.PreferenceProvider
import com.serviceapp.kia.databinding.ListServiceTypeBinding
import com.serviceapp.kia.utils.APP_ARB_LOCALE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/*
 *Created by Adithya T Raj on 06-07-2021
*/

class ServiceTypeAdapter(
    val clickListener: (ServiceTypeDataResponse) -> Unit
): RecyclerView.Adapter<ServiceTypeAdapter.ViewHolder>() {

    private var viewPosition: Int? = null

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    private val diffCallBack = object : DiffUtil.ItemCallback<ServiceTypeDataResponse>() {
        override fun areItemsTheSame(
            oldItem: ServiceTypeDataResponse,
            newItem: ServiceTypeDataResponse
        ): Boolean {
            return oldItem.servicetype_id == newItem.servicetype_id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ServiceTypeDataResponse,
            newItem: ServiceTypeDataResponse
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallBack)

    fun submitList(list: List<ServiceTypeDataResponse>) {
        viewPosition = null
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                differ.submitList(list.toList())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        val serviceName = when (PreferenceProvider(MainApplication.appContext).getLocale()) {
            APP_ARB_LOCALE -> {
                item.servicetype_name_arab
            }
            else -> {
                item.servicetype_name
            }
        }
        holder.binding.listServiceTypeTxt.text = serviceName
        holder.binding.listServiceTypeTxt.setOnClickListener {
            clickListener(item)
            viewPosition = position
            notifyDataSetChanged()
        }
        when (viewPosition) {
            position -> {
                holder.binding.listServiceTypeTxt.setTextColor(Color.parseColor("#FFFFFFFF"))
                holder.binding.listServiceTypeTxt.setBackgroundColor(Color.parseColor("#0c1325"))
            }
            else -> {
                holder.binding.listServiceTypeTxt.setTextColor(Color.parseColor("#0c1325"))
                holder.binding.listServiceTypeTxt.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    class ViewHolder(
        val binding: ListServiceTypeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListServiceTypeBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }


}