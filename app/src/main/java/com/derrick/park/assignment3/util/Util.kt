package com.derrick.park.assignment3.util

import androidx.core.text.isDigitsOnly

fun isValidCell(cell: String) = cell.length == 10 && cell.isDigitsOnly()

fun isValidName(name: String): Boolean = name.split("\\s+".toRegex()).size == 2

fun isSaveButtonEnabled(cell: String, name: String) = isValidCell(cell) && isValidName(name)