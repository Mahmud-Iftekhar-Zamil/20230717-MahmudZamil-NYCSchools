package com.example.nycschools.utils

sealed class Events {
    object Success: Events()
    data class Error(val error: Exception): Events()
}
