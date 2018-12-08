package tampan.kucingapes.simplewallpaper.helper

import android.content.Context
import android.net.Uri
import android.support.v4.content.FileProvider
import java.io.File

object UriProvider {
    fun getFileUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context,
                "tampan.kucingapes.simplewallpaper.fileprovider", file)
    }
}