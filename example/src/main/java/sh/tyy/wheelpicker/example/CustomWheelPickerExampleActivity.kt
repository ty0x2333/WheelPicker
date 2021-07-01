package sh.tyy.wheelpicker.example

import android.os.Bundle
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import sh.tyy.wheelpicker.core.BaseWheelPickerView
import sh.tyy.wheelpicker.example.custom.CustomWheelPickerView

class CustomWheelPickerExampleActivity : AppCompatActivity(), PickerExample {

    private lateinit var pickerView: CustomWheelPickerView
    override val circularCheckBox: CheckBox
        get() = findViewById(R.id.circular_check_box)
    override val selectedItemTextView: TextView
        get() = findViewById(R.id.selected_text_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_wheel_picker)
        title = "Custom"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        pickerView = findViewById(R.id.custom_picker_view)
        pickerView.adapter.values = (0 until 20).map {
            CustomWheelPickerView.Item(
                "$it",
                if (it % 2 == 0) ContextCompat.getDrawable(
                    this,
                    R.drawable.search
                ) else ContextCompat.getDrawable(this, R.drawable.add),
                "Item-$it"
            )
        }

        circularCheckBox.setOnCheckedChangeListener { _, isChecked ->
            pickerView.isCircular = isChecked
        }

        pickerView.setWheelListener(object : BaseWheelPickerView.WheelPickerViewListener {
            override fun didSelectItem(picker: BaseWheelPickerView, index: Int) {
                selectedItemTextView.text =
                    "Selected Item: ${pickerView.adapter.values.getOrNull(index)}"
            }
        })

        pickerView.selectedIndex = 2
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}