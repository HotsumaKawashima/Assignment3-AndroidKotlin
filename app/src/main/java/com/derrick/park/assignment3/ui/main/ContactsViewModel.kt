package com.derrick.park.assignment3.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.derrick.park.assignment3.database.getDatabase
import com.derrick.park.assignment3.domain.Contact
import com.derrick.park.assignment3.repository.ContactsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

enum class ContactsApiStatus { LOADING, DONE, ERROR }

class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * The data source this ViewModel will fetch results from.
     */
    private val contactsRepository: ContactsRepository = ContactsRepository(getDatabase(application))

    /**
     * A list of contacts fetched on to the screen.
     */
    val contacts: LiveData<List<Contact>> = contactsRepository.contacts

    /**
     * This is the job for all coroutines started by this viewModel.
     * Cancelling this job will cancel all coroutines started by this viewModel.
     */
    private val viewModelJob = Job()

    /**
     * This is the main scope for all coroutines launched by this ViewModel
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by
     * calling viewModelJob.cancel()
     */
    private val viewModelScope = CoroutineScope(viewModelJob + Main)

    /**
     * Event triggered for network error.
     */
    private var _networkErrorStatus = MutableLiveData<ContactsApiStatus>(ContactsApiStatus.LOADING)
    val networkErrorStatus: LiveData<ContactsApiStatus>
        get() = _networkErrorStatus

    /**
     * Navigation to the DetailFragment
     */
    private val _navigateToContactDetail = MutableLiveData<Contact>()
    val navigateToContactDetail: LiveData<Contact>
        get() = _navigateToContactDetail

    init { }

    /**
     * Fetch and refresh contact list
     */
    fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                contactsRepository.refreshContacts()
                _networkErrorStatus.value = ContactsApiStatus.DONE

            } catch (networkError: IOException) {
                if (contacts.value!!.isEmpty()) {
                    // Toast
                    _networkErrorStatus.value = ContactsApiStatus.ERROR
                }
            }
        }
    }

    /**
     * Clear contact list
     */
    fun clearDataFromRepository() = viewModelScope.launch {
            contactsRepository.clearContacts()
    }

    fun onContactClicked(contact: Contact) {
        _navigateToContactDetail.value = contact
    }

    fun onContactDetailNavigated() {
        _navigateToContactDetail.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(val app: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
                return ContactsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}