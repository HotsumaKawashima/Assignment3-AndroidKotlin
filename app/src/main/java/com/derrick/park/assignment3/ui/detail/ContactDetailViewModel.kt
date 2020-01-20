package com.derrick.park.assignment3.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.derrick.park.assignment3.domain.Contact

class ContactDetailViewModel(contact: Contact) : ViewModel() {

    private val _contact = MutableLiveData<Contact>()
    val contact: LiveData<Contact>
        get() = _contact

    init {
        _contact.value = contact
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val contact: Contact) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ContactDetailViewModel::class.java)) {
                return ContactDetailViewModel(contact) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
