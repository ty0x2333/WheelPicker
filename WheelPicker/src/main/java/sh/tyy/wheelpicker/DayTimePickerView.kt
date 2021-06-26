package sh.tyy.wheelpicker

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import androidx.core.content.ContextCompat

class DayTimePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), BaseWheelPickerView.WheelPickerViewListener {

    interface Listener {
        fun didSelectData(day: Int, hour: Int, minute: Int)
    }

    private val highlightView: View = run {
        val view = View(context)
        view.background = ContextCompat.getDrawable(context, R.drawable.text_wheel_highlight_bg)
        view
    }
    private val dayPickerView: TextWheelPickerView
    private val hourPickerView: TextWheelPickerView
    private val minutePickerView: TextWheelPickerView

    private var listener: Listener? = null

    fun setWheelListener(listener: Listener) {
        this.listener = listener
    }

    var day: Int
        set(value) {
            dayPickerView.selectedIndex = value - 1
        }
        get() = dayPickerView.selectedIndex + 1

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

    var isCircular: Boolean = false
        set(value) {
            field = value
            dayPickerView.isCircular = value
            hourPickerView.isCircular = value
            minutePickerView.isCircular = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.day_time_picker_view, this, true)
        dayPickerView = findViewById(R.id.day_picker)
        dayPickerView.adapter.values = (1..31).map {
            TextWheelPickerView.Item(
                "$it",
                context.getString(R.string.day_time_picker_format_day, it)
            )
        }
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

        dayPickerView.setWheelListener(this)
        hourPickerView.setWheelListener(this)
        minutePickerView.setWheelListener(this)
    }

    // region BaseWheelPickerView.WheelPickerViewListener
    override fun didSelectItem(picker: BaseWheelPickerView, index: Int) {
        listener?.didSelectData(day, hour, minute)
    }
    // endregion
}