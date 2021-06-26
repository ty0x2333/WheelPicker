package sh.tyy.wheelpicker

import android.content.Context

class DayTimePicker(context: Context) : WheelPickerActionSheet<DayTimePickerView>(context) {
    init {
        setPickerView(DayTimePickerView(context))
    }
}