package sh.tyy.wheelpicker

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class WeekdayTimePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), BaseWheelPickerView.WheelPickerViewListener {

    interface Listener {
        fun didSelectData(weekday: Int, hour: Int, minute: Int)
    }

    private val highlightView: View = run {
        val view = View(context)
        view.background = ContextCompat.getDrawable(context, R.drawable.text_wheel_highlight_bg)
        view
    }
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

    private var listener: Listener? = null

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

    var isCircular: Boolean = false
        set(value) {
            field = value
            weekdayPickerView.isCircular = value
            hourPickerView.isCircular = value
            minutePickerView.isCircular = value
        }

    private val weekdayAdapter = TextWheelAdapter()
    private val hourAdapter = TextWheelAdapter()
    private val minuteAdapter = TextWheelAdapter()

    init {
        LayoutInflater.from(context).inflate(R.layout.day_time_picker_view, this, true)
        weekdayPickerView = findViewById(R.id.left_picker)
        weekdayPickerView.setAdapter(weekdayAdapter)

        hourPickerView = findViewById(R.id.mid_picker)
        hourPickerView.setAdapter(hourAdapter)
        hourAdapter.values = (0 until 24).map { TextWheelPickerView.Item("$it", "$it") }

        minutePickerView = findViewById(R.id.right_picker)
        minutePickerView.setAdapter(minuteAdapter)
        minuteAdapter.values =
            (0 until 60).map { TextWheelPickerView.Item("$it", "$it") }

        addView(highlightView)
        (highlightView.layoutParams as? LayoutParams)?.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height =
                context.resources.getDimensionPixelSize(R.dimen.text_wheel_picker_item_height)
            gravity = Gravity.CENTER_VERTICAL
        }
        refreshWeekdays()

        weekdayPickerView.setWheelListener(this)
        hourPickerView.setWheelListener(this)
        minutePickerView.setWheelListener(this)
    }

    fun setWheelListener(listener: Listener) {
        this.listener = listener
    }

    private fun refreshWeekdays() {
        weekdays = normalWeekdays.subList(
            normalWeekdays.indexOf(calendar.firstDayOfWeek),
            normalWeekdays.count()
        ) + normalWeekdays.subList(0, normalWeekdays.indexOf(calendar.firstDayOfWeek))
        val now = Date()
        calendar.time = now
        weekdayAdapter.values = (0 until weekdays.count()).mapIndexed { index, weekday ->
            calendar.set(Calendar.DAY_OF_WEEK, weekdays[index])
            TextWheelPickerView.Item(
                "$weekday",
                formatter.format(calendar.time)
            )
        }
    }

    // region BaseWheelPickerView.WheelPickerViewListener
    override fun didSelectItem(picker: BaseWheelPickerView, index: Int) {
        listener?.didSelectData(weekday, hour, minute)
    }
    // endregion
}