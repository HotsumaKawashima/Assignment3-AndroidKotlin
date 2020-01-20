package com.derrick.park.assignment3.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDao {
    @Insert
    fun insert(contact: ContactEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(contacts: List<ContactEntity>)

    @Update
    fun update(contact: ContactEntity)

    @Query("SELECT * FROM contacts WHERE name = :name COLLATE NOCASE")
    fun getContactByName(name: String): ContactEntity?

    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts() : LiveData<List<ContactEntity>>

    @Query("DELETE FROM contacts")
    fun clear()
}

@Database(entities = [ContactEntity::class], version = 1, exportSchema = false)
abstract class ContactDatabase : RoomDatabase() {
    abstract val contactDao: ContactDao
}

private lateinit var INSTANCE: ContactDatabase

fun getDatabase(context: Context): ContactDatabase {
    synchronized(ContactDatabase::class.java) {
        if (!::INSTANCE.isInitialized)  {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                ContactDatabase::class.java,
                "contacts_db").build()
        }
    }
    return INSTANCE
}