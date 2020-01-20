package com.derrick.park.assignment3

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.derrick.park.assignment3.database.ContactDao
import com.derrick.park.assignment3.database.ContactDatabase
import com.derrick.park.assignment3.database.ContactEntity
import com.derrick.park.assignment3.testutil.LiveDataTestUtil.getValue
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ContactsDBTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var contactDao: ContactDao
    private lateinit var db: ContactDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room
            .inMemoryDatabaseBuilder(
                context,
                ContactDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        contactDao = db.contactDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetContact() {
        val contact = ContactEntity(
            "12345",
            "Male",
            "Derrick Park",
            "Vancouver",
            "0000000000",
            "abc@gmail.com"
        )
        contactDao.insert(contact)
        val derr = contactDao.getContactByName("Derrick Park")
        assertEquals(derr?.name, "Derrick Park")
    }

    @Test
    @Throws(Exception::class)
    fun getAllContacts() {
        val contact1 = ContactEntity(
            "12345",
            "Male",
            "Derrick Park",
            "Vancouver",
            "0000000000",
            "abc@gmail.com"
        )
        val contact2 = ContactEntity(
            "12346",
            "Male",
            "Derrick Park",
            "Vancouver",
            "0000000000",
            "abc@gmail.com"
        )
        contactDao.insert(contact1)
        contactDao.insert(contact2)

        val liveContacts = contactDao.getAllContacts()
        val contacts = getValue(liveContacts)
        assertThat(contacts.size, `is`(2))
    }
}

