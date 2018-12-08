package tampan.kucingapes.simplewallpaper.ankoUi

import android.graphics.Typeface
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.helper.Utils

class DialogUi : AnkoComponent<BottomSheetDialog> {
    override fun createView(ui: AnkoContext<BottomSheetDialog>): View = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            linearLayout {
                padding = dip(8)
                gravity = Gravity.CENTER_VERTICAL
                backgroundColorResource = R.color.tintBg
                imageView {
                    id = R.id.author_img
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }.lparams(dip(30), dip(30))

                textView {
                    id = R.id.author_name
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams { margin = dip(6) }
            }.lparams(matchParent, wrapContent)

            verticalLayout {
                lparams(matchParent, wrapContent)
                leftPadding = dip(8)
                rightPadding = dip(8)

                linearLayout {
                    verticalLayout {
                        linearLayout {
                            id = R.id.author_insta
                            padding = dip(8)
                            gravity = Gravity.CENTER_VERTICAL
                            Utils.ripple(this)
                            imageView {
                                setImageResource(R.drawable.ic_social_insta)
                            }.lparams(dip(22), dip(22))

                            textView {
                                id = R.id.author_insta_text
                            }.lparams { margin = dip(6) }
                        }.lparams(wrapContent, wrapContent)


                        linearLayout {
                            padding = dip(8)
                            gravity = Gravity.CENTER_VERTICAL
                            cardView {
                                radius = 22f
                                cardElevation = 0f
                                id = R.id.color_accent
                            }.lparams(dip(22), dip(22))

                            textView("#ffff") {
                                id = R.id.color_accent_text
                            }.lparams { margin = dip(6) }
                        }.lparams(wrapContent, wrapContent)


                    }.lparams(dip(0), wrapContent) {
                        weight = 1f
                    }

                    linearLayout {
                        id = R.id.aspec_ratio
                        padding = dip(8)
                        gravity = Gravity.CENTER_VERTICAL
                        imageView {
                            setImageResource(R.drawable.ic_aspec_ratio)
                        }.lparams(wrapContent, wrapContent)

                        textView {
                            id = R.id.aspec_ratio_text
                        }.lparams { margin = dip(6) }
                    }.lparams(dip(0), wrapContent) {
                        weight = 1f
                    }
                }

                textView("Download options") {
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams {
                    bottomMargin = dip(6)
                    topMargin = dip(8)
                    leftMargin = dip(8)
                }
                linearLayout {
                    recyclerView {
                        id = R.id.list_download_col1
                        layoutManager = LinearLayoutManager(context)
                    }.lparams(dip(0), wrapContent) {
                        weight = 1f
                    }

                    recyclerView {
                        id = R.id.list_download_col2
                        layoutManager = LinearLayoutManager(context)
                    }.lparams(dip(0), wrapContent) {
                        weight = 1f
                    }

                }


                textView("Share options") {
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams {
                    bottomMargin = dip(6)
                    topMargin = dip(8)
                    leftMargin = dip(8)
                }

                linearLayout {
                    linearLayout {
                        id = R.id.copy_url
                        padding = dip(8)
                        gravity = Gravity.CENTER_VERTICAL
                        Utils.ripple(this)
                        imageView {
                            setImageResource(R.drawable.ic_copy)
                        }.lparams(wrapContent, wrapContent)

                        textView("Copy original url") {
                        }.lparams { margin = dip(6) }
                    }.lparams(dip(0), wrapContent) {
                        weight = 1f
                    }

                    linearLayout {
                        id = R.id.share_on
                        padding = dip(8)
                        gravity = Gravity.CENTER_VERTICAL
                        Utils.ripple(this)
                        imageView {
                            setImageResource(R.drawable.ic_share)
                        }.lparams(wrapContent, wrapContent)

                        textView("Share on") {
                        }.lparams { margin = dip(6) }
                    }.lparams(dip(0), wrapContent) {
                        weight = 1f
                    }

                }
            }
        }
    }
}