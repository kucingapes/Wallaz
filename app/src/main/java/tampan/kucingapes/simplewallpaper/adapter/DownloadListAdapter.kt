package tampan.kucingapes.simplewallpaper.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find
import tampan.kucingapes.simplewallpaper.ankoUi.DownloadUi
import tampan.kucingapes.simplewallpaper.helper.IDownloadClickListener
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.model.download.DownldModel

class DownloadListAdapter(private val downloads: MutableList<DownldModel>, private val downloadClickListener: IDownloadClickListener) : RecyclerView.Adapter<DownloadListAdapter.Holder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        return Holder(DownloadUi().createView(AnkoContext.create(context, parent, false)))
    }

    override fun getItemCount(): Int = downloads.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val download = downloads[position]

        val downloadBadge: ImageView = holder.itemView.find(R.id.download_badge)
        val downloadText: TextView = holder.itemView.find(R.id.download_text)

        downloadText.text = download.type
        downloadBadge.setImageResource(download.badge as Int)

        holder.itemView.setOnClickListener {
            downloadClickListener.onDownloadClickListener(download)
        }
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}