package com.derrick.park.assignment3.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * A retrofit service to fetch a contact list.
 */
interface ContactApiService {
    @GET("?nat=ca")
    fun getContactsAsync(@Query("results") num: Int): Deferred<ContactList>
}

/**
 * Main entry point for network access.
 * call like `ContactNetwork.contacts.getContacts()`
 */
object ContactNetwork {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://randomuser.me/api/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val contacts: ContactApiService = retrofit.create(ContactApiService::class.java)
}