package sh.tyy.wheelpicker.example

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import sh.tyy.wheelpicker.BaseWheelPickerView
import sh.tyy.wheelpicker.TextWheelPickerView
import java.util.*

class MainActivity : AppCompatActivity(), PickerExample {
    private lateinit var pickerView: TextWheelPickerView
    override lateinit var selectedItemTextView: TextView
    override lateinit var circularCheckBox: CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pickerView = findViewById(R.id.picker_view)

        pickerView.adapter.values = (0 until 20).map { TextWheelPickerView.Item("$it", "index-$it") }
        selectedItemTextView = findViewById(R.id.selected_text_view)
        circularCheckBox = findViewById(R.id.circular_check_box)
        pickerView.setWheelListener(object: BaseWheelPickerView.WheelPickerViewListener {
            override fun didSelectItem(picker: BaseWheelPickerView, index: Int) {
                updateSelectedText(index)
            }
        })

        circularCheckBox.setOnCheckedChangeListener { _, isChecked ->
            pickerView.isCircular = isChecked
        }

        val dayTimePickerButton: Button = findViewById(R.id.day_time_picker_button)
        dayTimePickerButton.setOnClickListener {
            val intent = Intent(this, DayTimePickerExampleActivity::class.java)
            startActivity(intent)
        }

        val weekdayTimePickerButton: Button = findViewById(R.id.weekday_time_picker_button)
        weekdayTimePickerButton.setOnClickListener {
            val intent = Intent(this, WeekdayTimePickerExampleActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateSelectedText(selectedIndex: Int) {
        val text = pickerView.adapter.values.getOrNull(selectedIndex)
        selectedItemTextView.text = "Selected: $text"
    }
}