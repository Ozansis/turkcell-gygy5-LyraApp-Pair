package com.turkcell.lyraapp.data.download

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class DownloadRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : DownloadRepository {

    private val downloadManager =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    override fun startDownload(songId: String, streamUrl: String, title: String): Long {
        val destDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC) ?: return -1L
        val destFile = File(destDir, "${songId}.wav")
        if (destFile.exists()) return -1L

        val request = DownloadManager.Request(Uri.parse(streamUrl))
            .setTitle(title)
            .setDescription("Lyra - indiriliyor")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationUri(Uri.fromFile(destFile))
            .setAllowedOverRoaming(false)

        return downloadManager.enqueue(request)
    }

    override fun isDownloaded(songId: String): Boolean {
        val destDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC) ?: return false
        return File(destDir, "${songId}.wav").exists()
    }

    override fun getLocalFileUri(songId: String): Uri? {
        val destDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC) ?: return null
        val file = File(destDir, "${songId}.wav")
        return if (file.exists()) Uri.fromFile(file) else null
    }

    override fun getDownloadStatus(downloadId: Long): Int {
        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor = downloadManager.query(query)
        return if (cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
            cursor.close()
            status
        } else {
            cursor.close()
            DownloadManager.STATUS_FAILED
        }
    }
}
