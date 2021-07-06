package sh.tyy.wheelpicker.example

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.ConfigurationCompat
import sh.tyy.wheelpicker.DayTimePicker
import sh.tyy.wheelpicker.WeekdayTimePicker
import sh.tyy.wheelpicker.WeekdayTimePickerView
import java.text.SimpleDateFormat
import java.util.*

class WeekdayTimePickerExampleActivity : AppCompatActivity(), PickerExample {

    private lateinit var weekdayTimePickerView: WeekdayTimePickerView
    override val circularCheckBox: CheckBox
        get() = findViewById(R.id.circular_check_box)
    override val selectedItemTextView: TextView
        get() = findViewById(R.id.selected_text_view)
    override val vibrationFeedbackCheckBox: CheckBox
        get() = findViewById(R.id.vibration_feedback_check_box)

    private val formatter = SimpleDateFormat("EEE HH:mm")
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weekday_time_picker)
        title = "Weekday Time"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        weekdayTimePickerView = findViewById(R.id.weekday_time_picker_view)

        circularCheckBox.setOnCheckedChangeListener { _, isChecked ->
            weekdayTimePickerView.isCircular = isChecked
        }

        vibrationFeedbackCheckBox.isChecked = weekdayTimePickerView.isHapticFeedbackEnabled
        vibrationFeedbackCheckBox.setOnCheckedChangeListener { _, isChecked ->
            weekdayTimePickerView.isHapticFeedbackEnabled = isChecked
        }

        weekdayTimePickerView.setWheelListener(object : WeekdayTimePickerView.Listener {
            override fun didSelectData(weekday: Int, hour: Int, minute: Int) {
                calendar.set(Calendar.DAY_OF_WEEK, weekday)
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                selectedItemTextView.text = formatter.format(calendar.time)
            }
        })

        setupWeekdayTimePicker()

        val actionSheetButton: Button = findViewById(R.id.action_sheet_button)
        actionSheetButton.setOnClickListener {
            val picker = WeekdayTimePicker(this)
            picker.show(window)
            picker.pickerView?.weekday = weekdayTimePickerView.weekday
            picker.pickerView?.hour = weekdayTimePickerView.hour
            picker.pickerView?.minute = weekdayTimePickerView.minute
            picker.setOnClickOkButtonListener {
                val pickerView = picker.pickerView ?: return@setOnClickOkButtonListener
                weekdayTimePickerView.weekday = pickerView.weekday
                weekdayTimePickerView.hour = pickerView.hour
                weekdayTimePickerView.minute = pickerView.minute
                picker.hide()
            }
            picker.setOnDismissListener {
                Toast.makeText(this, "Action Sheet Dismiss", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupWeekdayTimePicker() {
        calendar.time = Date()
        if (ConfigurationCompat.getLocales(resources.configuration).get(0).country.equals("CN")) {
            weekdayTimePickerView.firstDayOfWeek = Calendar.MONDAY
        }

        weekdayTimePickerView.weekday = calendar.get(Calendar.DAY_OF_WEEK)
        weekdayTimePickerView.hour = calendar.get(Calendar.HOUR_OF_DAY)
        weekdayTimePickerView.minute = calendar.get(Calendar.MINUTE)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}