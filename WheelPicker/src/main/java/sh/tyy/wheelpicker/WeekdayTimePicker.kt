package sh.tyy.wheelpicker

import android.content.Context

class WeekdayTimePicker(context: Context) : WheelPickerActionSheet<WeekdayTimePickerView>(context) {
    init {
        setPickerView(WeekdayTimePickerView(context))
    }
}