package tampan.kucingapes.simplewallpaper.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.*
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.androidnetworking.error.ANError
import com.github.chrisbanes.photoview.PhotoView
import com.kucingapes.ankodrawer.AnDrawerClickListener
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.design.indefiniteSnackbar
import org.jetbrains.anko.design.snackbar
import tampan.kucingapes.simplewallpaper.helper.DownloadTag
import tampan.kucingapes.simplewallpaper.helper.IDownloadClickListener
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.adapter.DownloadListAdapter
import tampan.kucingapes.simplewallpaper.ankoUi.DialogUi
import tampan.kucingapes.simplewallpaper.ankoUi.ImgUi
import tampan.kucingapes.simplewallpaper.fab_behavior.BottomViewOffset
import tampan.kucingapes.simplewallpaper.i_view.IImageView
import tampan.kucingapes.simplewallpaper.i_view.ISingleView
import tampan.kucingapes.simplewallpaper.model.photos.Unsplash
import tampan.kucingapes.simplewallpaper.model.download.DownldModel
import tampan.kucingapes.simplewallpaper.presenter.ImagePresenter
import tampan.kucingapes.simplewallpaper.presenter.SinglePresenter

class ImageActivity : BaseActivity(), AnDrawerClickListener, ISingleView, IDownloadClickListener, IImageView {

    private lateinit var toolbar: Toolbar
    private lateinit var fabMenu: FloatingActionButton
    private lateinit var fabHq: FloatingActionButton
    private lateinit var photoView: PhotoView
    private lateinit var appBar: AppBarLayout
    private lateinit var parent: CoordinatorLayout
    private lateinit var bottomUi: LinearLayout
    private lateinit var dialog: BottomSheetDialog

    private lateinit var singlePresenter: SinglePresenter
    private lateinit var imagePresenter: ImagePresenter
    private lateinit var id: String

    private var author: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImgUi().setContentView(this)

        bindId()
        initAppBar()
        initPresenter()
    }

    private fun initAppBar() {
        appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            photoView.setOnClickListener {
                if (verticalOffset == 0) appBarLayout.setExpanded(false)
                else appBarLayout.setExpanded(true)
            }
        }

        val syncBottomUi = BottomViewOffset(parent, bottomUi)
        appBar.addOnOffsetChangedListener(syncBottomUi)
    }

    private fun initPresenter() {
        singlePresenter = SinglePresenter(this)
        singlePresenter.getData(id)

        imagePresenter = ImagePresenter(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onLoadSingle(unsplash: Unsplash) {
        dialogDownload(unsplash)

        toolbar.navigationIconResource = R.drawable.ic_back
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Photo by ${unsplash.user?.firstName}"

        Picasso.get()
                .load(unsplash.urls?.small)
                .into(photoView)

        fabHq.setOnClickListener {
            Picasso.get().load(unsplash.urls?.regular).into(photoView)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun dialogDownload(unsplash: Unsplash) {
        author = unsplash.user?.name
        dialog = BottomSheetDialog(this)
        dialog.setContentView(DialogUi().createView(AnkoContext.create(dialog.context, dialog)))

        val user = dialog.find(R.id.author_name) as TextView
        val userImg = dialog.find(R.id.author_img) as ImageView
        val userInsta = dialog.find(R.id.author_insta) as LinearLayout
        val userInstaText = dialog.find(R.id.author_insta_text) as TextView
        val aspecRation = dialog.find(R.id.aspec_ratio_text) as TextView
        val colorDot = dialog.find(R.id.color_accent) as CardView
        val colorText = dialog.find(R.id.color_accent_text) as TextView
        val copy = dialog.find(R.id.copy_url) as LinearLayout
        val share = dialog.find(R.id.share_on) as LinearLayout

        val downloadListCol1 = dialog.find(R.id.list_download_col1) as RecyclerView
        val downloadListCol2 = dialog.find(R.id.list_download_col2) as RecyclerView

        user.text = "Photo by $author\nfrom Unsplash"
        userInstaText.text = unsplash.user?.instagramUsername
        aspecRation.text = "${unsplash.height} x ${unsplash.width}"
        Picasso.get()
                .load(unsplash.user?.profileImage?.medium)
                .transform(CropCircleTransformation())
                .into(userImg)

        if (userInstaText.text == "null" || userInstaText.text == "") {
            userInsta.visibility = View.GONE
        }

        userInsta.setOnClickListener {
            browse("https://instagram.com/${unsplash.user?.instagramUsername}")
        }

        colorDot.setCardBackgroundColor(Color.parseColor(unsplash.color as String))
        colorText.text = unsplash.color

        val downloadsCol1: MutableList<DownldModel> = mutableListOf()
        downloadsCol1.add(DownldModel("Medium size", R.drawable.ic_download, unsplash.urls?.regular as String, DownloadTag.MEDIUM))
        downloadsCol1.add(DownldModel("Full size", R.drawable.ic_download, unsplash.urls.full as String, DownloadTag.FULL))
        downloadsCol1.add(DownldModel("Raw size", R.drawable.ic_download, unsplash.urls.raw as String, DownloadTag.RAW))

        val downloadsCol2: MutableList<DownldModel> = mutableListOf()
        downloadsCol2.add(DownldModel("Add Live Wallpaper", R.drawable.ic_live_wallpaper, unsplash.urls.regular, DownloadTag.LIVE))
        downloadsCol2.add(DownldModel("Set As Wallpaper", R.drawable.ic_wallpaper, unsplash.urls.regular, DownloadTag.WALLPAPER))

        downloadListCol1.adapter = DownloadListAdapter(downloadsCol1, this)
        downloadListCol2.adapter = DownloadListAdapter(downloadsCol2, this)

        copy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val dataClip = ClipData.newPlainText("copy", unsplash.links?.html)
            clipboard.primaryClip = dataClip
            dialog.dismiss()
            parent.snackbar("Url copied")
        }

        share.setOnClickListener {
            imagePresenter.apply {
                setDescShare("Photo by ${unsplash.user?.name}\nGet from Unsplash\n${unsplash.links?.html}")
                requestImage(unsplash.urls.small as String, DownloadTag.SHARE, "", "")
            }
        }

        fabMenu.setOnClickListener {
            dialog.show()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onError(anError: ANError) {
        error { "unsplash error -----> ${anError.errorDetail}" }
        val progress = find(R.id.progress_circular) as ProgressBar
        val textError = find(R.id.error_text) as TextView

        progress.visibility = View.GONE
        textError.visibility = View.VISIBLE
        textError.text = "Error, code: ${anError.errorDetail}"
    }

    override fun onDownloadClickListener(downldModel: DownldModel) {
        dialog.dismiss()

        when (downldModel.tag) {
            DownloadTag.MEDIUM -> {
                imagePresenter.requestImage(downldModel.url as String, DownloadTag.MEDIUM, id, author)
            }
            DownloadTag.FULL -> {
                imagePresenter.requestImage(downldModel.url as String, DownloadTag.FULL, id, author)
            }
            DownloadTag.RAW -> {
                imagePresenter.requestImage(downldModel.url as String, DownloadTag.RAW, id, author)
            }
            DownloadTag.LIVE -> {
                imagePresenter.requestImage(downldModel.url as String, DownloadTag.LIVE, id, author)
            }
            DownloadTag.WALLPAPER -> {
                imagePresenter.requestImage(downldModel.url as String, DownloadTag.WALLPAPER, id, author)
            }
            else -> {
            }
        }
    }

    override fun onDownloadComplete() {
        parent.snackbar("Download Complete")
    }

    override fun onDownloadError(anError: ANError) {
        error { "error_bro -----> ${anError.errorDetail}" }

        parent.snackbar("Download Error, code: \n${anError.message}")
    }

    override fun onFileExist() {
        parent.snackbar("File exist")
    }

    override fun onLiveSuccess() {
        parent.indefiniteSnackbar("Wallpaper added to live wallpaper", "Config") {
            startActivity<ChangerActivity>()
        }.setActionTextColor(resources.getColor(R.color.colorPrimary))
    }

    override fun onLiveExist() {
        parent.indefiniteSnackbar("Wallpaper exist on live wallpaper", "Config") {
            startActivity<ChangerActivity>()
        }.setActionTextColor(resources.getColor(R.color.colorPrimary))
    }

    private fun bindId() {
        id = intent.getStringExtra("id")

        toolbar = find(R.id.toolbar)
        fabMenu = find(R.id.fab_menu)
        fabHq = find(R.id.fab_hq)
        photoView = find(R.id.img_view)
        appBar = find(R.id.app_bar)
        parent = find(R.id.parent)
        bottomUi = find(R.id.bottom_ui)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return false
    }

}