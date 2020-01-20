package com.derrick.park.assignment3.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.derrick.park.assignment3.R
import com.derrick.park.assignment3.databinding.ListItemContactBinding
import com.derrick.park.assignment3.domain.Contact
import kotlinx.android.synthetic.main.list_item_header.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val ITEM_VIEW_TYPE_HEADER = 0
const val ITEM_VIEW_TYPE_CONTACT = 1

/**
 * RecyclerView Adapter for setting up data binding on the items in the list.
 */
class ContactsAdapter(private val onContactClickListener: OnContactClickListener)
    : ListAdapter<DataItem, RecyclerView.ViewHolder>(ContactDiffCallback()) {

    private val adapterScope = CoroutineScope(Default)

    var contacts: List<Contact> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val withDataBinding: ListItemContactBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ContactViewHolder.LAYOUT,
            parent,
            false
        )
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_CONTACT -> ContactViewHolder(withDataBinding)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContactViewHolder -> {
                holder.holderDataBinding.apply {
                    contact = (getItem(position) as DataItem.ContactItem).contact
                    contactClickListener = onContactClickListener
                    executePendingBindings()
                }
            }
            is HeaderViewHolder -> {
                holder.bind(getItem(position).id)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.ContactItem -> ITEM_VIEW_TYPE_CONTACT
        }
    }

    fun addHeaderAndSubmitList(contacts: List<Contact>) {
        adapterScope.launch {
            val items = ArrayList<DataItem>()
            if (contacts.isNotEmpty()) {
                var headerChar = contacts[0].name.first().toUpperCase()
                items.add(DataItem.Header(headerChar))
                for (item in contacts) {
                    val char = item.name.first().toUpperCase()
                    if (char != headerChar) {
                        headerChar = char
                        items.add(DataItem.Header(headerChar))
                    }
                    items.add(DataItem.ContactItem(item))
                }
            }
            withContext(Main) {
                submitList(items)
            }
        }
    }

    /**
     * ViewHolder for contact items. All work is done by data binding.
     */
    class ContactViewHolder(val holderDataBinding: ListItemContactBinding) : RecyclerView.ViewHolder(holderDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_item_contact
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(index: String) {
            itemView.header_tv.text = index
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.list_item_header, parent, false)
                return HeaderViewHolder(view)
            }
        }
    }
}

class OnContactClickListener(val clickListener: (contact: Contact) -> Unit) {
    fun onClick(contact: Contact) = clickListener(contact)
}

class ContactDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean = oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean = oldItem == newItem
}

sealed class DataItem {
    abstract val id: String

    data class ContactItem(val contact: Contact) : DataItem() {
        override val id: String
            get() = contact.id
    }

    data class Header(val index: Char) : DataItem() {
        override val id = index.toString()
    }
}
