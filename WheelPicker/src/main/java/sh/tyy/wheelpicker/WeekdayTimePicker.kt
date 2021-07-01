package sh.tyy.wheelpicker

import android.content.Context
import sh.tyy.wheelpicker.core.WheelPickerActionSheet

class WeekdayTimePicker(context: Context) : WheelPickerActionSheet<WeekdayTimePickerView>(context) {
    init {
        setPickerView(WeekdayTimePickerView(context))
    }
}