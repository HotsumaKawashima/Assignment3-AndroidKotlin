package com.derrick.park.assignment3.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.derrick.park.assignment3.database.ContactDatabase
import com.derrick.park.assignment3.database.ContactEntity
import com.derrick.park.assignment3.database.asDomainModel
import com.derrick.park.assignment3.domain.Contact
import com.derrick.park.assignment3.network.ContactNetwork
import com.derrick.park.assignment3.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Repository for fetching contacts from the network and storing
 * them on disk
 */
class ContactsRepository(private val database: ContactDatabase) {

    val contacts: LiveData<List<Contact>> = Transformations.map(database.contactDao.getAllContacts()) {
        it.asDomainModel()
    }

    /**
     * Refresh the contacts stored in the offline cache
     */
    suspend fun refreshContacts() {
        withContext(IO) {
            Timber.d("refreshContacts() called")
            database.contactDao.clear()
            val contacts = ContactNetwork.contacts.getContactsAsync(20).await()
            database.contactDao.insertAll(contacts.asDatabaseModel())
        }
    }

    /**
     * Clear contacts
     */
    suspend fun clearContacts() {
        withContext(IO) {
            database.contactDao.clear()
        }
    }

    /**
     * Save a new contact
     */
    suspend fun saveContact(contact: ContactEntity) {
        withContext(IO) {
            database.contactDao.insert(contact)
        }
    }
}