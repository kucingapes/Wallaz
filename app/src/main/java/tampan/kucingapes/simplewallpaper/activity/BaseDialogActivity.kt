package tampan.kucingapes.simplewallpaper.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.design.widget.BottomSheetDialog
import android.view.View
import android.widget.*
import org.jetbrains.anko.find
import tampan.kucingapes.simplewallpaper.helper.ChangerHelper
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.model.SpinnerWithInt
import java.io.File

@SuppressLint("Registered")
open class BaseDialogActivity : BaseActivity() {

    lateinit var dialogChanger: BottomSheetDialog

    private lateinit var spinnerValue: SpinnerWithInt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    @SuppressLint("SetTextI18n")
    fun dialogChanger() {
        val status = dialogChanger.find(R.id.status) as TextView
        val spinner = dialogChanger.find(R.id.spinner_interval) as Spinner
        val switcher = dialogChanger.find(R.id.switcher) as Switch

        val sharedPreferences = getSharedPreferences("changer", Context.MODE_PRIVATE)
        val savedLong = sharedPreferences.getLong("interval", 0)
        val selectedPosition = sharedPreferences.getInt("positionSpinner", 0)

        val path = File(Environment.getExternalStorageDirectory(), ".wallaz/auto")
        val files = path.listFiles()
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

        switcher.setOnCheckedChangeListener { buttonView, isChecked ->
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

        val minute = 60000
        val oneHour = 60 * minute
        val oneDay = 24 * oneHour

        val intervals: MutableList<SpinnerWithInt> = mutableListOf()
        intervals.add(SpinnerWithInt("3 detik", 3000))
        intervals.add(SpinnerWithInt("5 detik", 5000))
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