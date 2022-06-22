package com.serviceapp.kia.ui.fragments.appointment.slot

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.serviceapp.kia.databinding.ListServiceTypeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/*
 *Created by Adithya T Raj on 06-07-2021
*/

class SlotAppointmentAdapter(
    val clickListener: (String) -> Unit
): RecyclerView.Adapter<SlotAppointmentAdapter.ViewHolder>() {

    private val slotList = arrayListOf<String>()
    private var viewPosition: Int? = null

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun submitList(list: List<String>) {
        viewPosition = null
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                slotList.clear()
                slotList.addAll(list)
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = slotList[position]
        holder.binding.listServiceTypeTxt.text = item
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

    override fun getItemCount() = slotList.size

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