package tampan.kucingapes.simplewallpaper.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView
import tampan.kucingapes.simplewallpaper.helper.ChangerHelper
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.ankoUi.ChangerUi
import tampan.kucingapes.simplewallpaper.model.SpinnerWithInt
import java.io.File
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import tampan.kucingapes.simplewallpaper.adapter.ImageChangerAdapter


class ChangerActivity : BaseActivity() {

    private lateinit var status: TextView
    private lateinit var spinner: Spinner
    private lateinit var switcher: Switch

    private lateinit var spinnerValue: SpinnerWithInt
    private lateinit var sharedPreferences: SharedPreferences
    private var savedLong: Long = 0
    private var selectedPosition: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ChangerUi().setContentView(this)

        status = find(R.id.status)
        spinner = find(R.id.spinner_interval)
        switcher = find(R.id.switcher)

        initToolbar()
        initSpinner()

        sharedPreferences = getSharedPreferences("changer", Context.MODE_PRIVATE)
        savedLong = sharedPreferences.getLong("interval", 0)
        selectedPosition = sharedPreferences.getInt("positionSpinner", 0)

        val path = "${Environment.getExternalStorageDirectory().absolutePath}/${getString(R.string.path_changer)}"
        val folder = File(path)

        val files = folder.listFiles()
        val sumFiles = files.size

        if (sumFiles <= 1) {
            status.text = "Not found wallpaper"
            spinner.isEnabled = false
            switcher.isEnabled = false
        }

        if (sumFiles == 1) {
            status.text = "1 wallpaper found, cannot activated"
            spinner.isEnabled = false
            switcher.isEnabled = false
        }

        if (sumFiles > 1) {
            status.text = "${files.size} wallpapers can be mixed"
            spinner.isEnabled = true
            switcher.isEnabled = true
        }

        switcher.isChecked = savedLong != 0L

        switcher.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                ChangerHelper.startServiceChanger(this, spinnerValue.interval.toLong())
                sharedPreferences.edit().apply {
                    putLong("interval", spinnerValue.interval.toLong())
                    putInt("positionSpinner", spinner.selectedItemPosition)
                }.apply()
            } else {
                ChangerHelper.stopServiceChanger(this)
                sharedPreferences.edit().putLong("interval", 0).apply()
            }
        }

        initImg(files)
    }

    private fun initImg(files: Array<File>) {

        val listImage: MutableList<File> = mutableListOf()
        listImage.addAll(files)

        val recyclerImg = find(R.id.recycler_view) as RecyclerView
        val adapterImg = ImageChangerAdapter(listImage)
        recyclerImg.apply {
            layoutManager = GridLayoutManager(this@ChangerActivity, 3)
            adapter = adapterImg
        }

    }

    private fun initToolbar() {
        val toolbar = find(R.id.toolbar) as Toolbar
        toolbar.title = "Auto Changer Wallpaper"
        toolbar.navigationIconResource = R.drawable.ic_back
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initSpinner() {
        val minute = 60000
        val oneHour = 60 * minute
        val oneDay = 24 * oneHour

        val intervals: MutableList<SpinnerWithInt> = mutableListOf()
        intervals.add(SpinnerWithInt("5 minute", 5 * minute))
        intervals.add(SpinnerWithInt("1 hour", oneHour))
        intervals.add(SpinnerWithInt("3 hour", 3 * oneHour))
        intervals.add(SpinnerWithInt("6 hour", 6 * oneHour))
        intervals.add(SpinnerWithInt("1 day", oneDay))

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                intervals)
        spinner.adapter = spinnerAdapter

        Handler().postDelayed({
            spinner.setSelection(selectedPosition, false)
        }, 300)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerValue = parent?.getItemAtPosition(position) as SpinnerWithInt
                sharedPreferences.edit().putInt("position_spinner", position).apply()
            }

        }
    }
}