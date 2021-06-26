package sh.tyy.wheelpicker

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import java.text.SimpleDateFormat
import java.util.*

class WeekdayTimePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val highlightView: View = Utils.buildHighlightView(context)
    private val weekdayPickerView: TextWheelPickerView
    private val hourPickerView: TextWheelPickerView
    private val minutePickerView: TextWheelPickerView

    private val normalWeekdays = listOf(
        Calendar.SUNDAY,
        Calendar.MONDAY,
        Calendar.TUESDAY,
        Calendar.WEDNESDAY,
        Calendar.THURSDAY,
        Calendar.FRIDAY,
        Calendar.SATURDAY
    )

    private var weekdays: List<Int> = normalWeekdays

    var weekday: Int
        set(value) {
            weekdayPickerView.selectedIndex = weekdays.indexOf(value)
        }
        get() = weekdays.getOrNull(weekdayPickerView.selectedIndex) ?: firstDayOfWeek

    var hour: Int
        set(value) {
            hourPickerView.selectedIndex = value
        }
        get() = hourPickerView.selectedIndex

    var minute: Int
        set(value) {
            minutePickerView.selectedIndex = value
        }
        get() = minutePickerView.selectedIndex

    private val formatter = SimpleDateFormat("EEE")
    private val calendar: Calendar = Calendar.getInstance()
    var firstDayOfWeek: Int
        set(value) {
            calendar.firstDayOfWeek = value
            refreshWeekdays()
        }
        get() = calendar.firstDayOfWeek

    init {
        LayoutInflater.from(context).inflate(R.layout.day_time_picker_view, this, true)
        weekdayPickerView = findViewById(R.id.day_picker)
        hourPickerView = findViewById(R.id.hour_picker)
        hourPickerView.adapter.values = (0 until 24).map { TextWheelPickerView.Item("$it", "$it") }
        minutePickerView = findViewById(R.id.minute_picker)
        minutePickerView.adapter.values =
            (0 until 60).map { TextWheelPickerView.Item("$it", "$it") }
        addView(highlightView)
        (highlightView.layoutParams as? LayoutParams)?.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height =
                context.resources.getDimensionPixelSize(R.dimen.text_wheel_picker_item_height)
            gravity = Gravity.CENTER_VERTICAL
        }
        refreshWeekdays()
    }

    private fun refreshWeekdays() {
        weekdays = normalWeekdays.subList(
            normalWeekdays.indexOf(calendar.firstDayOfWeek),
            normalWeekdays.count()
        ) + normalWeekdays.subList(0, normalWeekdays.indexOf(calendar.firstDayOfWeek))
        val now = Date()
        calendar.time = now
        weekdayPickerView.adapter.values = (0 until weekdays.count()).mapIndexed { index, weekday ->
            calendar.set(Calendar.DAY_OF_WEEK, weekdays[index])
            TextWheelPickerView.Item(
                "$weekday",
                formatter.format(calendar.time)
            )
        }
    }
}