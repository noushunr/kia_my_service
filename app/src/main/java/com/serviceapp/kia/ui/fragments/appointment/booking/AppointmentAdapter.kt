package com.serviceapp.kia.ui.fragments.appointment.booking

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.serviceapp.kia.MainApplication
import com.serviceapp.kia.data.network.responses.ServicesApi.*
import com.serviceapp.kia.data.preferences.PreferenceProvider
import com.serviceapp.kia.databinding.ListServiceBinding
import com.serviceapp.kia.utils.APP_ARB_LOCALE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/*
 *Created by Adithya T Raj on 06-07-2021
*/

class AppointmentAdapter(
    val clickListener: (ServicesDataResponse) -> Unit
): RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    private val diffCallBack = object : DiffUtil.ItemCallback<ServicesDataResponse>() {
        override fun areItemsTheSame(
            oldItem: ServicesDataResponse,
            newItem: ServicesDataResponse
        ): Boolean {
            return oldItem.service_id == newItem.service_id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ServicesDataResponse,
            newItem: ServicesDataResponse
        ): Boolean {
            return oldItem.isChecked == newItem.isChecked
        }

    }

    private val differ = AsyncListDiffer(this, diffCallBack)

    fun submitList(list: List<ServicesDataResponse>) {
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
                item.service_name_arab
            }
            else -> {
                item.service_name
            }
        }
        holder.binding.listServiceRadioBtn.text = serviceName
        /*holder.binding.listServiceRadioBtn.setOnCheckedChangeListener { _, boolean ->
            if (boolean) {
                clickListener(item)
            }
        }*/
        holder.binding.listServiceRadioBtn.setOnClickListener {
            clickListener(item)
        }
        holder.binding.listServiceRadioBtn.isChecked = item.isChecked
    }

    override fun getItemCount() = differ.currentList.size

    class ViewHolder(
        val binding: ListServiceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListServiceBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }


}