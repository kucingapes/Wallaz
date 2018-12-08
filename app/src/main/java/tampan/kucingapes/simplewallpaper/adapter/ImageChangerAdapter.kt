package tampan.kucingapes.simplewallpaper.adapter

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.helper.UriProvider
import tampan.kucingapes.simplewallpaper.activity.ChangerActivity
import java.io.File

class ImageChangerAdapter(private val files: MutableList<File>) : RecyclerView.Adapter<ImageChangerAdapter.Holder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        return Holder(ImageListUi().createView(AnkoContext.create(context, parent)))
    }

    override fun getItemCount(): Int = files.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val file = files[position]
        val img = holder.itemView.find(R.id.img_view) as ImageView
        val uri = UriProvider.getFileUri(context, file)

        Picasso.get().load(uri).resize(150, 150).into(img)

        holder.itemView.setOnLongClickListener {
            context.alert("Delete this image?") {
                positiveButton("Delete") {
                    file.delete()
                    refreshActivity(context)
                }
            }.show()
            true
        }
    }

    private fun refreshActivity(context: Context) {
        context as AppCompatActivity
        context.finish()
        context.startActivity<ChangerActivity>()
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    class ImageListUi : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
            cardView {
                lparams(dip(100), dip(100)) {
                    margin = dip(6)
                }
                radius = 0f
                imageView {
                    id = R.id.img_view
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }.lparams(matchParent, matchParent)
            }
        }
    }
}