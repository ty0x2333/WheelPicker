package sh.tyy.wheelpicker

import android.content.Context

class DatePicker(context: Context) : WheelPickerActionSheet<DatePickerView>(context) {
    init {
        setPickerView(DatePickerView(context))
    }
}