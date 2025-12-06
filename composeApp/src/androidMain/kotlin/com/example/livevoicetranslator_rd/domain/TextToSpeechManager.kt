package com.example.livevoicetranslator_rd.domain

import android.app.Activity

private var _activityProvider: () -> Activity? = {
    null
}

val activityProvider: () -> Activity?
    get() = _activityProvider

fun setActivityProvider(provider: () -> Activity?) {
    _activityProvider = provider
}