package com.derrick.park.assignment3.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.derrick.park.assignment3.database.ContactEntity
import com.derrick.park.assignment3.repository.ContactsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class AddContactViewModel(val contactsRepository: ContactsRepository) : ViewModel() {

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
     * Two-way data-binding, exposing MutableLiveData
     */
    val name = MutableLiveData<String>()

    /**
     * Two-way data-binding, exposing MutableLiveData
     */
    val cell = MutableLiveData<String>()

    /**
     * Naviation to the ContactsFragment
     */
    private val _navigateToContacts = MutableLiveData<Boolean?>()
    val navigateToContacts: LiveData<Boolean?>
        get() = _navigateToContacts

    fun onSaveClicked() {
        viewModelScope.launch {
            contactsRepository.saveContact(
                // TODO: Can be improved
                ContactEntity(
                    id = UUID.randomUUID().toString(),
                    gender = null,
                    name = name.value!!,
                    location = null,
                    cell = cell.value!!,
                    email = null)
            )
            _navigateToContacts.value = true
        }
    }

    fun onContactsNavigated() {
        _navigateToContacts.value = null
    }

    fun onCancelClicked() {
        _navigateToContacts.value = true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(val repository: ContactsRepository) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddContactViewModel::class.java)) {
                return AddContactViewModel(repository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}