package sh.tyy.wheelpicker

import android.content.Context
import sh.tyy.wheelpicker.core.WheelPickerActionSheet

class DatePicker(context: Context) : WheelPickerActionSheet<DatePickerView>(context) {
    init {
        setPickerView(DatePickerView(context))
    }
}