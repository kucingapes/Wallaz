package tampan.kucingapes.simplewallpaper.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import tampan.kucingapes.simplewallpaper.helper.LayoutType
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.ankoUi.PreferencesUi
import tampan.kucingapes.simplewallpaper.model.SpinnerWithType

class PreferencesActivity : AppCompatActivity() {
    private lateinit var spinner: Spinner
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferencesUi().setContentView(this)

        spinner = find(R.id.layout_type)
        sharedPref = getSharedPreferences("layout", Context.MODE_PRIVATE)

        initSpinner()

        val toolbar = find(R.id.toolbar) as Toolbar
        toolbar.title = "Preferences"
        toolbar.navigationIconResource = R.drawable.ic_back
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initSpinner() {
        val layouts: MutableList<SpinnerWithType> = mutableListOf()
        layouts.add(SpinnerWithType("Grid", LayoutType.GRID))
        layouts.add(SpinnerWithType("Vertical", LayoutType.VERTICAL))
        layouts.add(SpinnerWithType("Grid with card", LayoutType.GRID_CARD))
        layouts.add(SpinnerWithType("Vertical with card", LayoutType.VERTICAL_CARD))

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                layouts)
        spinner.adapter = spinnerAdapter
        spinner.setSelection(sharedPref.getInt("position_spinner", 0))
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sharedPref.edit().putInt("position_spinner", position).apply()
                val value = parent?.getItemAtPosition(position) as SpinnerWithType
                sharedPref.edit().putString("type", value.type).apply()
            }
        }
    }

    override fun onBackPressed() {
        startActivity<MainActivity>()
        finish()
    }
}