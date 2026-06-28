package com.turkcell.lyraapp.data.download

import android.net.Uri

interface DownloadRepository {
    fun startDownload(songId: String, streamUrl: String, title: String): Long
    fun isDownloaded(songId: String): Boolean
    fun getLocalFileUri(songId: String): Uri?
    fun getDownloadStatus(downloadId: Long): Int
}
