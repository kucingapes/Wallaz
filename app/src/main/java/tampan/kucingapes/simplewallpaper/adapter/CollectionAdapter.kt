package tampan.kucingapes.simplewallpaper.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import tampan.kucingapes.simplewallpaper.helper.LayoutType
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.helper.Utils
import tampan.kucingapes.simplewallpaper.activity.InCollectionActivity
import tampan.kucingapes.simplewallpaper.model.collection.UnsplashCollection

class CollectionAdapter(private val list: MutableList<UnsplashCollection>) : RecyclerView.Adapter<CollectionAdapter.Holder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        return Holder(CollectionListUi().createView(AnkoContext.create(context, parent)))
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val collection = list[position]
        val header = holder.itemView.find(R.id.img_main) as ImageView
        val collectionTitle = holder.itemView.find(R.id.title) as TextView
        val collectionAuthor = holder.itemView.find(R.id.author_name) as TextView
        val collectionSize = holder.itemView.find(R.id.collection_size) as TextView

        Picasso.get().load(collection.coverPhoto?.urls?.small).into(header)

        collectionTitle.text = collection.title
        collectionAuthor.text = collection.user?.name
        collectionSize.text = "${collection.totalPhotos} photos"

        holder.itemView.setOnClickListener {
            val intent = Intent(context, InCollectionActivity::class.java)
            intent.putExtra("id_collection", "collections/${collection.id}/")
            intent.putExtra("title", collection.title)
            val option  = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as AppCompatActivity,
                    collectionTitle,
                    ViewCompat.getTransitionName(collectionTitle)
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                context.startActivity(intent, option.toBundle())
            } else context.startActivity<InCollectionActivity>(
                    "id_collection" to "collections/${collection.id}/",
                    "title" to collection.title)
        }
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    class CollectionListUi : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
            cardView {

                val sharedPref = context?.getSharedPreferences("layout", Context.MODE_PRIVATE)
                val layoutType = sharedPref?.getString("type", LayoutType.GRID)

                radius = 0f

                lparams(matchParent, dip(300))

                when (layoutType) {
                    LayoutType.GRID_CARD -> {
                        lparams(matchParent, dip(300)) {
                            margin = dip(6)
                        }
                    }
                    LayoutType.VERTICAL_CARD -> {
                        lparams(matchParent, dip(300)) {
                            margin = dip(6)
                        }
                    }
                }

                Utils.ripple(this)

                imageView {
                    id = R.id.img_main
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }

                verticalLayout {
                    padding = dip(12)
                    backgroundColorResource = R.color.colorPrimary
                    textView {
                        typeface = Typeface.DEFAULT_BOLD
                        id = R.id.title
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            transitionName = "title"
                        }
                    }
                    textView {
                        id = R.id.author_name
                    }
                    textView {
                        id = R.id.collection_size
                    }
                }.lparams(matchParent, wrapContent) {
                    gravity = Gravity.BOTTOM
                }
            }
        }
    }
}