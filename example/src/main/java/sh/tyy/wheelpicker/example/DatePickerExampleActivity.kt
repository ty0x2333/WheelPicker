package sh.tyy.wheelpicker.example

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
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

    private var formatter = SimpleDateFormat("yyyy-MM-dd")
    private val calendar = Calendar.getInstance()

    private lateinit var maxDateTextField: TextInputEditText
    private lateinit var minDateTextField: TextInputEditText

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
                updateSelectedText(year, month, day)
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

        minDateTextField = findViewById(R.id.min_date_text_field)
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
                    updateLimitTextFieldsText()
                } catch (e: Throwable) {
                    datePickerView.minDate = null
                }
            }
        })

        maxDateTextField = findViewById(R.id.max_date_text_field)
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
                    updateLimitTextFieldsText()
                } catch (e: Throwable) {
                    datePickerView.maxDate = null
                }
            }
        })

        setupRadioGroup()
    }

    private fun setupRadioGroup() {
        val yearMonthDayButton: RadioButton = findViewById(R.id.year_month_day_button)
        val yearMonthButton: RadioButton = findViewById(R.id.year_month_button)
        when (datePickerView.mode) {
            DatePickerView.Mode.YEAR_MONTH_DAY -> yearMonthDayButton.isChecked = true
            DatePickerView.Mode.YEAR_MONTH -> yearMonthButton.isChecked = true
        }
        yearMonthDayButton.setOnClickListener {
            datePickerView.mode = DatePickerView.Mode.YEAR_MONTH_DAY
            formatter = SimpleDateFormat("yyyy-MM-dd")
            updateSelectedText(datePickerView.year, datePickerView.month, datePickerView.day)
            updateLimitTextFieldsText()
        }
        yearMonthButton.setOnClickListener {
            datePickerView.mode = DatePickerView.Mode.YEAR_MONTH
            formatter = SimpleDateFormat("yyyy-MM")
            updateSelectedText(datePickerView.year, datePickerView.month, datePickerView.day)
            updateLimitTextFieldsText()
        }
    }

    private fun updateSelectedText(year: Int, month: Int, day: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        selectedItemTextView.text = formatter.format(calendar.time)
    }

    private fun updateLimitTextFieldsText() {
        val minDateText = datePickerView.minDate?.let {
            formatter.format(it)
        } ?: ""
        if (minDateTextField.text?.toString() != minDateText) {
            minDateTextField.setText(minDateText)
        }

        val maxDateText = datePickerView.maxDate?.let {
            formatter.format(it)
        } ?: ""
        if (maxDateTextField.text?.toString() != maxDateText) {
            maxDateTextField.setText(maxDateText)
        }
    }

    private fun showPicker(completion: (year: Int, month: Int, day: Int) -> Unit) {
        val picker = DatePicker(this)
        picker.show(window)
        picker.pickerView?.mode = datePickerView.mode
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