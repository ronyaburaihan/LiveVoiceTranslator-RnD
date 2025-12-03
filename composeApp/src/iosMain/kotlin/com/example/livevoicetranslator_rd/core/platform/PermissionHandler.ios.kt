package com.example.livevoicetranslator_rd.core.platform

import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeAudio
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHAuthorizationStatusRestricted
import platform.Photos.PHPhotoLibrary
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class PermissionHandler {
    actual suspend fun requestPermission(permission: Permission): PermissionStatus {
        return when (permission) {
            Permission.CAMERA -> requestCameraPermission()
            Permission.PHOTO_LIBRARY -> requestPhotoLibraryPermission()
            Permission.MICROPHONE -> requestMicrophonePermission()
        }
    }

    actual suspend fun checkPermission(permission: Permission): PermissionStatus {
        return when (permission) {
            Permission.CAMERA -> checkCameraPermission()
            Permission.PHOTO_LIBRARY -> checkPhotoLibraryPermission()
            Permission.MICROPHONE -> checkMicrophonePermission()
        }
    }

    private suspend fun requestCameraPermission(): PermissionStatus = suspendCoroutine { cont ->
        AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
            cont.resume(if (granted) PermissionStatus.GRANTED else PermissionStatus.DENIED)
        }
    }

    private fun checkCameraPermission(): PermissionStatus {
        return when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)) {
            AVAuthorizationStatusAuthorized -> PermissionStatus.GRANTED
            AVAuthorizationStatusDenied, AVAuthorizationStatusRestricted -> PermissionStatus.DENIED
            else -> PermissionStatus.NOT_DETERMINED
        }
    }

    private suspend fun requestPhotoLibraryPermission(): PermissionStatus = suspendCoroutine { cont ->
        PHPhotoLibrary.requestAuthorization { status ->
            val result = when (status) {
                PHAuthorizationStatusAuthorized, PHAuthorizationStatusLimited -> PermissionStatus.GRANTED
                else -> PermissionStatus.DENIED
            }
            cont.resume(result)
        }
    }

    private fun checkPhotoLibraryPermission(): PermissionStatus {
        return when (PHPhotoLibrary.authorizationStatus()) {
            PHAuthorizationStatusAuthorized, PHAuthorizationStatusLimited -> PermissionStatus.GRANTED
            PHAuthorizationStatusDenied, PHAuthorizationStatusRestricted -> PermissionStatus.DENIED
            else -> PermissionStatus.NOT_DETERMINED
        }
    }

    private suspend fun requestMicrophonePermission(): PermissionStatus = suspendCoroutine { cont ->
        AVCaptureDevice.requestAccessForMediaType(AVMediaTypeAudio) { granted ->
            cont.resume(if (granted) PermissionStatus.GRANTED else PermissionStatus.DENIED)
        }
    }

    private fun checkMicrophonePermission(): PermissionStatus {
        return when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeAudio)) {
            AVAuthorizationStatusAuthorized -> PermissionStatus.GRANTED
            AVAuthorizationStatusDenied, AVAuthorizationStatusRestricted -> PermissionStatus.DENIED
            else -> PermissionStatus.NOT_DETERMINED
        }
    }
}