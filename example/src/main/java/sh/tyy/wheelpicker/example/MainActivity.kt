package sh.tyy.wheelpicker.example

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import sh.tyy.wheelpicker.BaseWheelPickerView
import sh.tyy.wheelpicker.TextWheelAdapter
import sh.tyy.wheelpicker.TextWheelPickerView

class MainActivity : AppCompatActivity(), PickerExample {
    private lateinit var pickerView: TextWheelPickerView
    override lateinit var selectedItemTextView: TextView
    override lateinit var circularCheckBox: CheckBox


    private val simpleAdapter = TextWheelAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pickerView = findViewById(R.id.picker_view)
        pickerView.setAdapter(simpleAdapter)
        simpleAdapter.values = (0 until 20).map { TextWheelPickerView.Item("$it", "index-$it") }
        selectedItemTextView = findViewById(R.id.selected_text_view)
        circularCheckBox = findViewById(R.id.circular_check_box)
        pickerView.setWheelListener(object : BaseWheelPickerView.WheelPickerViewListener {
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

        val customPickerButton: Button = findViewById(R.id.custom_picker_button)
        customPickerButton.setOnClickListener {
            val intent = Intent(this, CustomWheelPickerExampleActivity::class.java)
            startActivity(intent)
        }

        val datePickerButton: Button = findViewById(R.id.date_picker_button)
        datePickerButton.setOnClickListener {
            val intent = Intent(this, DatePickerExampleActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateSelectedText(selectedIndex: Int) {
        val text = simpleAdapter.values.getOrNull(selectedIndex)
        selectedItemTextView.text = "Selected: $text"
    }
}