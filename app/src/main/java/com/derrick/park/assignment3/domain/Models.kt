package com.derrick.park.assignment3.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Domain objects are plain Kotlin data classes that represent the things in our app. These are the
 * objects that should be displayed on screen, or manipulated by the app.
 *
 * @see db package for objects that are mapped to the database
 * @see network package for objects that parse or prepare network calls
 */

/**
 * Contact object that represents a contact info
 */
@Parcelize
data class Contact(val id: String,
                   val gender: String?,
                   val name: String,
                   val location: String?,
                   val cell: String,
                   val email: String?) : Parcelable

