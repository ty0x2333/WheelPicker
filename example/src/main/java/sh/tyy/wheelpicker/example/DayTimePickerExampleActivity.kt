package sh.tyy.wheelpicker.example

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sh.tyy.wheelpicker.DayTimePicker
import sh.tyy.wheelpicker.DayTimePickerView
import java.text.SimpleDateFormat
import java.util.*

class DayTimePickerExampleActivity : AppCompatActivity(), PickerExample {

    private lateinit var dayTimePickerView: DayTimePickerView
    override val circularCheckBox: CheckBox
        get() = findViewById(R.id.circular_check_box)
    override val vibrationFeedbackCheckBox: CheckBox
        get() = findViewById(R.id.vibration_feedback_check_box)
    override val selectedItemTextView: TextView
        get() = findViewById(R.id.selected_text_view)

    private val formatter = SimpleDateFormat("dd HH:mm")
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_time_picker)
        title = "Day Time"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        dayTimePickerView = findViewById(R.id.day_time_picker_view)
        vibrationFeedbackCheckBox.isChecked = dayTimePickerView.isHapticFeedbackEnabled
        vibrationFeedbackCheckBox.setOnCheckedChangeListener { _, isChecked ->
            dayTimePickerView.isHapticFeedbackEnabled = isChecked
        }

        circularCheckBox.setOnCheckedChangeListener { _, isChecked ->
            dayTimePickerView.isCircular = isChecked
        }

        dayTimePickerView.setWheelListener(object : DayTimePickerView.Listener {
            override fun didSelectData(day: Int, hour: Int, minute: Int) {
                calendar.set(Calendar.DAY_OF_MONTH, day)
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                selectedItemTextView.text = formatter.format(calendar.time)
            }
        })

        setupDayTimePicker()

        val actionSheetButton: Button = findViewById(R.id.action_sheet_button)
        actionSheetButton.setOnClickListener {
            val picker = DayTimePicker(this)
            picker.show(window)
            picker.pickerView?.day = dayTimePickerView.day
            picker.pickerView?.hour = dayTimePickerView.hour
            picker.pickerView?.minute = dayTimePickerView.minute
            picker.setOnClickOkButtonListener {
                val pickerView = picker.pickerView ?: return@setOnClickOkButtonListener
                dayTimePickerView.day = pickerView.day
                dayTimePickerView.hour = pickerView.hour
                dayTimePickerView.minute = pickerView.minute
                picker.hide()
            }
            picker.setOnDismissListener {
                Toast.makeText(this, "Action Sheet Dismiss", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupDayTimePicker() {
        calendar.time = Date()
        dayTimePickerView.day = calendar.get(Calendar.DAY_OF_MONTH)
        dayTimePickerView.hour = calendar.get(Calendar.HOUR_OF_DAY)
        dayTimePickerView.minute = calendar.get(Calendar.MINUTE)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}