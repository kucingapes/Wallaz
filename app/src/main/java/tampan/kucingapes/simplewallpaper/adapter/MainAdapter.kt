package tampan.kucingapes.simplewallpaper.adapter

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import tampan.kucingapes.simplewallpaper.activity.ImageActivity
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.helper.Utils
import tampan.kucingapes.simplewallpaper.model.photos.Unsplash
import android.support.v4.view.ViewCompat
import android.support.v4.app.ActivityOptionsCompat
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import tampan.kucingapes.simplewallpaper.helper.LayoutType


class MainAdapter(private val list: MutableList<Unsplash>) : RecyclerView.Adapter<MainAdapter.Holder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        return Holder(
            MainListUi().createView(
                AnkoContext.create(context, parent, false)
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val unsplash = list[position]

        val imgView = holder.itemView.find<ImageView>(R.id.img_view)
        Picasso.get().load(unsplash.urls?.small).into(imgView)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ImageActivity::class.java)
            intent.putExtra("id", unsplash.id)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as AppCompatActivity,
                imgView,
                ViewCompat.getTransitionName(imgView)
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                context.startActivity(intent, options.toBundle())
            } else context.startActivity<ImageActivity>("id" to unsplash.id)

        }
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    class MainListUi : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
            cardView {
                val sharedPref = context?.getSharedPreferences("layout", Context.MODE_PRIVATE)
                val layoutType = sharedPref?.getString("type", LayoutType.GRID)

                Utils.ripple(this)

                radius = 0f

                when (layoutType) {
                    LayoutType.GRID -> lparams(matchParent, dip(300))
                    LayoutType.GRID_CARD -> {
                        lparams(matchParent, dip(300)) {
                            margin = dip(6)
                        }
                    }
                    LayoutType.VERTICAL -> lparams(matchParent, wrapContent)
                    LayoutType.VERTICAL_CARD -> {
                        lparams(matchParent, wrapContent) {
                            margin = dip(6)
                        }
                    }
                }

                imageView {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        transitionName = "image"
                    }
                    id = R.id.img_view

                    when (layoutType) {
                        LayoutType.GRID -> scaleType = ImageView.ScaleType.CENTER_CROP
                        LayoutType.GRID_CARD -> scaleType = ImageView.ScaleType.CENTER_CROP
                        else -> adjustViewBounds = true
                    }

                }.lparams(matchParent, matchParent)
            }
        }
    }
}