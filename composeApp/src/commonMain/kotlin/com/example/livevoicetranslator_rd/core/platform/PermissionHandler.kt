package com.example.livevoicetranslator_rd.core.platform

enum class Permission {
    CAMERA,
    PHOTO_LIBRARY,
    MICROPHONE
}

expect class PermissionHandler {
    suspend fun requestPermission(permission: Permission): PermissionStatus
    suspend fun checkPermission(permission: Permission): PermissionStatus
}