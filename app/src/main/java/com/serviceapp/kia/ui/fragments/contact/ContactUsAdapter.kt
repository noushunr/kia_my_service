package com.serviceapp.kia.ui.fragments.contact

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.serviceapp.kia.data.network.responses.ContactApi.ContactDataResponse
import com.serviceapp.kia.databinding.ListContactsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/*
 *Created by Adithya T Raj on 20-02-2022
*/

class ContactUsAdapter(
    private val clickListener: (ContactDataResponse) -> Unit
): RecyclerView.Adapter<ContactUsAdapter.ViewHolder>() {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    private val diffCallBack = object : DiffUtil.ItemCallback<ContactDataResponse>() {
        override fun areItemsTheSame(
            oldItem: ContactDataResponse,
            newItem: ContactDataResponse
        ): Boolean {
            return oldItem.contact_id == newItem.contact_id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ContactDataResponse,
            newItem: ContactDataResponse
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallBack)

    fun submitList(list: List<ContactDataResponse>) {
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
        private val binding: ListContactsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ContactDataResponse, clickListener: (ContactDataResponse) -> Unit) {
            binding.contactAddressTxt.text = item.contact_address
            binding.contactNumberTxt.text = item.contact_phon
            binding.contactEmailTxt.text = item.contact_mail
            binding.contactUrlTxt.text = item.contact_location_url
            binding.contactCallBtn.setOnClickListener {
                clickListener(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListContactsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }


}