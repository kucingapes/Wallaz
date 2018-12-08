package tampan.kucingapes.simplewallpaper.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.DownloadListener
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import tampan.kucingapes.simplewallpaper.helper.DownloadTag
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.helper.UriProvider
import tampan.kucingapes.simplewallpaper.i_view.IImageView
import java.io.File

class ImagePresenter(private val context: Context) : AnkoLogger {
    val view = context as IImageView
    private lateinit var notificationManagerCompat: NotificationManagerCompat
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private var idNotification = 0

    private var shareDesc = "Download from unsplash"

    fun setDescShare(shareDesc: String): String {
        this.shareDesc = shareDesc
        return shareDesc
    }

    fun requestImage(url: String, tag: DownloadTag, name: String, author: String?) {
        val folderName = "$tag"

        // generate random int id from name
        idNotification = try {
            java.lang.Long.parseLong(name.subSequence(0, 8).toString(), 36).toInt()
        } catch (e: Exception) {
            1234
        }


        notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationBuilder = NotificationCompat.Builder(context, "id").apply {
            setContentTitle("Download image")
            setContentText("from $author")
            setSmallIcon(R.drawable.ic_wallpaper)
            priority = NotificationCompat.PRIORITY_MAX
        }

        val picturePath = Environment.getExternalStorageDirectory().absolutePath +
                "/${Environment.DIRECTORY_PICTURES}" +
                "/Wallaz" +
                "/$folderName"

        val file = File(picturePath, "$name.jpg")

        when (tag) {
            DownloadTag.WALLPAPER -> {
                val mediumPath = Environment.getExternalStorageDirectory().absolutePath +
                        "/${Environment.DIRECTORY_PICTURES}" +
                        "/Wallaz" +
                        "/MEDIUM"
                val fileWallpaper = File(mediumPath, "$name.jpg")
                if (fileWallpaper.exists()) {
                    setupWallpaper(fileWallpaper)
                } else {
                    downloadStart(url, mediumPath, name, fileWallpaper, DownloadTag.WALLPAPER)
                }
            }
            DownloadTag.LIVE -> {
                val liveImagePath = Environment.getExternalStorageDirectory().absolutePath +
                        "/.wallaz/auto"
                val fileLiveImage = File(liveImagePath, "$name.jpg")

                if (fileLiveImage.exists()) {
                    view.onLiveExist()
                } else {
                    downloadStart(url, liveImagePath, name, fileLiveImage, DownloadTag.LIVE)
                }
            }
            DownloadTag.SHARE -> {
                val cachePath = Environment.getExternalStorageDirectory().absolutePath +
                        "/.wallaz/cache"
                val cacheFile = File(cachePath, "share.jpg")
                downloadStart(url, cachePath, "share", cacheFile, DownloadTag.SHARE)
            }
            else -> {
                if (file.exists()) {
                    view.onFileExist()
                } else {
                    downloadStart(url, picturePath, name, file, tag)
                }
            }
        }
    }

    private fun downloadStart(url: String, picturePath: String, name: String, file: File, tag: DownloadTag) {
        AndroidNetworking.download(url, picturePath, "$name.jpg")
                .build()
                .setDownloadProgressListener { bytesDownloaded, totalBytes ->

                    if (tag != DownloadTag.SHARE) {
                        notificationManagerCompat.apply {
                            notificationBuilder.setProgress(totalBytes.toInt(), bytesDownloaded.toInt(), false)
                            notificationBuilder.setOnlyAlertOnce(true)
                            notify(idNotification, notificationBuilder.build())
                        }

                        if (bytesDownloaded == totalBytes) {
                            notificationManagerCompat.cancel(idNotification)
                        }
                    }

                }
                .startDownload(object : DownloadListener {
                    override fun onDownloadComplete() {
                        view.onDownloadComplete()

                        val mediaScan = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                        mediaScan.data = Uri.fromFile(file)
                        context.sendBroadcast(mediaScan)

                        if (tag != DownloadTag.SHARE) {
                            notificationManagerCompat.apply {
                                notificationBuilder.setContentTitle("Download Complete")
                                        .setProgress(0, 0, false)
                                notify(idNotification, notificationBuilder.build())
                            }
                        }

                        when (tag) {
                            DownloadTag.WALLPAPER -> setupWallpaper(file)
                            DownloadTag.LIVE -> view.onLiveSuccess()
                            DownloadTag.SHARE -> shareImage(file, shareDesc)
                            else -> {
                            }
                        }
                    }

                    override fun onError(anError: ANError) {
                        view.onDownloadError(anError)
                        if (tag != DownloadTag.SHARE) {
                            notificationManagerCompat.apply {
                                notificationBuilder.setContentTitle("Download Error")
                                        .setProgress(0, 0, false)
                                notify(idNotification, notificationBuilder.build())
                            }
                        }
                    }

                })
    }

    private fun setupWallpaper(file: File) {
        val uri = UriProvider.getFileUri(context, file)
        try {
            val intent = Intent(Intent.ACTION_ATTACH_DATA).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                setDataAndType(uri, "image/*")
                putExtra("mimeType", "image/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(intent, "Set as"))
            info { "wallpaper setup sukses" }
        } catch (e: Exception) {
            error { "wallpaper setup error -----> ${e.message}" }
        }
    }

    private fun shareImage(file: File, data: String) {
        val uri = UriProvider.getFileUri(context, file)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            setDataAndType(uri, context.contentResolver.getType(uri))
            putExtra(Intent.EXTRA_TEXT, data)
            type = "image/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Choose app"))
    }

}
