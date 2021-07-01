package sh.tyy.wheelpicker.example

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import sh.tyy.wheelpicker.DatePicker
import sh.tyy.wheelpicker.DatePickerView
import java.text.SimpleDateFormat
import java.util.*

class DatePickerExampleActivity : AppCompatActivity(), PickerExample {

    private lateinit var datePickerView: DatePickerView
    override val circularCheckBox: CheckBox
        get() = findViewById(R.id.circular_check_box)
    override val selectedItemTextView: TextView
        get() = findViewById(R.id.selected_text_view)

    private val formatter = SimpleDateFormat("yyyy-MM-dd")
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker)
        title = "Date"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        datePickerView = findViewById(R.id.day_time_picker_view)

        circularCheckBox.setOnCheckedChangeListener { _, isChecked ->
            datePickerView.isCircular = isChecked
        }

        datePickerView.setWheelListener(object : DatePickerView.Listener {
            override fun didSelectData(year: Int, month: Int, day: Int) {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                selectedItemTextView.text = formatter.format(calendar.time)
            }
        })

        val actionSheetButton: Button = findViewById(R.id.action_sheet_button)
        actionSheetButton.setOnClickListener {
            showPicker { year, month, day ->
                datePickerView.year = year
                datePickerView.month = month
                datePickerView.day = day
            }
        }

        val minDateTextField: TextInputEditText = findViewById(R.id.min_date_text_field)
        minDateTextField.setOnClickListener {
            showPicker { year, month, day ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                minDateTextField.setText(formatter.format(calendar.time))
            }
        }
        minDateTextField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    val date = formatter.parse(s.toString())
                    datePickerView.minDate = date
                    val text = datePickerView.minDate?.let {
                        formatter.format(it)
                    } ?: ""
                    if (minDateTextField.text?.toString() == text) {
                        return
                    }
                    minDateTextField.setText(text)
                } catch (e: Throwable) {
                    datePickerView.minDate = null
                }
            }
        })

        val maxDateTextField: TextInputEditText = findViewById(R.id.max_date_text_field)
        maxDateTextField.setOnClickListener {
            showPicker { year, month, day ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                maxDateTextField.setText(formatter.format(calendar.time))
            }
        }

        maxDateTextField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    val date = formatter.parse(s.toString())
                    datePickerView.maxDate = date
                    val text = datePickerView.maxDate?.let {
                        formatter.format(it)
                    } ?: ""
                    if (maxDateTextField.text?.toString() == text) {
                        return
                    }
                    maxDateTextField.setText(text)
                } catch (e: Throwable) {
                    datePickerView.maxDate = null
                }
            }
        })
    }

    private fun showPicker(completion: (year: Int, month: Int, day: Int) -> Unit) {
        val picker = DatePicker(this)
        picker.show(window)
        picker.pickerView?.apply {
            year = datePickerView.year
            month = datePickerView.month
            day = datePickerView.day
        }
        picker.setOnClickOkButtonListener {
            val pickerView = picker.pickerView ?: return@setOnClickOkButtonListener
            completion(pickerView.year, pickerView.month, pickerView.day)
            picker.hide()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}