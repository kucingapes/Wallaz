package tampan.kucingapes.simplewallpaper.i_view

import com.androidnetworking.error.ANError

interface IImageView {
    fun onDownloadComplete()
    fun onDownloadError(anError: ANError)
    fun onLiveSuccess()
    fun onLiveExist()
    fun onFileExist()
}