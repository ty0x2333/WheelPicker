package sh.tyy.wheelpicker

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.set
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import java.lang.ref.WeakReference
import java.text.DateFormatSymbols
import java.util.*

open class DateWheelAdapter(
    protected val valueEnabledProvider: WeakReference<ValueEnabledProvider>
) :
    BaseWheelPickerView.Adapter<TextWheelPickerView.Item, TextWheelViewHolder>() {
    interface ValueEnabledProvider {
        fun isEnabled(adapter: DateWheelAdapter, valueIndex: Int): Boolean
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextWheelViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.wheel_picker_item, parent, false) as TextView
        return TextWheelViewHolder(view)
    }

    override fun onBindViewHolder(holder: TextWheelViewHolder, position: Int) {
        val valueIndex = if (isCircular) position % values.count() else position
        val value =
            values.getOrNull(valueIndex) ?: return
        val isEnabled = valueEnabledProvider.get()?.isEnabled(this, valueIndex) ?: true
        holder.onBindData(
            TextWheelPickerView.Item(
                id = "$position",
                text = value.text,
                isEnabled = isEnabled
            )
        )
    }
}

class YearWheelAdapter(
    valueEnabledProvider: WeakReference<ValueEnabledProvider>
) :
    DateWheelAdapter(valueEnabledProvider) {
    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override val valueCount: Int
        get() = Int.MAX_VALUE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextWheelViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.wheel_picker_item, parent, false) as TextView
        return TextWheelViewHolder(view)
    }

    override fun onBindViewHolder(holder: TextWheelViewHolder, position: Int) {
        val text = SpannableString("$position")
        val isEnabled = valueEnabledProvider.get()?.isEnabled(this, position) ?: true
        holder.onBindData(
            TextWheelPickerView.Item(
                id = "$position",
                text = text,
                isEnabled = isEnabled
            )
        )
    }
}

class DatePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), BaseWheelPickerView.WheelPickerViewListener,
    DateWheelAdapter.ValueEnabledProvider {

    data class Data(
        val year: Int,
        val month: Int,
        val day: Int
    )

    private fun minDateData(): Data? {
        if (minDate == null) {
            return null
        }
        return Data(
            year = minDateCalendar.get(Calendar.YEAR),
            month = minDateCalendar.get(Calendar.MONTH),
            day = minDateCalendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun maxDateData(): Data? {
        if (maxDate == null) {
            return null
        }
        return Data(
            year = maxDateCalendar.get(Calendar.YEAR),
            month = maxDateCalendar.get(Calendar.MONTH),
            day = maxDateCalendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    interface Listener {
        fun didSelectData(year: Int, month: Int, day: Int)
    }

    private val highlightView: View = run {
        val view = View(context)
        view.background = ContextCompat.getDrawable(context, R.drawable.text_wheel_highlight_bg)
        view
    }
    private val yearPickerView: TextWheelPickerView
    private val monthPickerView: TextWheelPickerView
    private val dayPickerView: TextWheelPickerView

    private var listener: Listener? = null
    private val minDateCalendar: Calendar = Calendar.getInstance()
    private val maxDateCalendar: Calendar = Calendar.getInstance()

    fun setWheelListener(listener: Listener) {
        this.listener = listener
    }

    var minDate: Date? = null
        set(value) {
            val newValue =
                if (value != null && maxDate != null) minOf(maxDate ?: value, value) else value
            val oldData = minDateData()
            if (field == newValue) {
                return
            }
            field = newValue
            newValue?.let {
                minDateCalendar.time = it
            }
            val newData = minDateData()
            if (newValue != null) {
                updateDateByMinDate(minDateCalendar)
            }
            reloadPickerIfNeeded(oldData, newData)
        }

    var maxDate: Date? = null
        set(value) {
            val newValue =
                if (value != null && minDate != null) maxOf(minDate ?: value, value) else value
            val oldData = maxDateData()
            if (field == newValue) {
                return
            }
            field = newValue
            newValue?.let {
                maxDateCalendar.time = it
            }
            val newData = maxDateData()
            if (newValue != null) {
                updateDateByMaxDate(maxDateCalendar)
            }
            reloadPickerIfNeeded(oldData, newData)
        }

    private fun reloadPickerIfNeeded(oldData: Data?, newData: Data?) {
        if (oldData?.year != newData?.year) {
            yearAdapter.notifyDataSetChanged()
        }
        if (year == newData?.year &&
            (!(newData.year == oldData?.year && newData.month == oldData.month))
        ) {
            monthAdapter.notifyDataSetChanged()
        }
        if (year == newData?.year && month == newData.month &&
            !(newData.year == oldData?.year && newData.month == oldData.month && newData.day == oldData.day)
        ) {
            dayAdapter.notifyDataSetChanged()
        }
    }

    var day: Int
        set(value) {
            setDay(value, false)
        }
        get() = dayPickerView.selectedIndex + 1

    var month: Int
        set(value) {
            setMonth(value, false)
        }
        get() = monthPickerView.selectedIndex

    var year: Int
        set(value) {
            setYear(value, false)
        }
        get() = yearPickerView.selectedIndex

    var isCircular: Boolean = false
        set(value) {
            field = value
            dayPickerView.isCircular = value
            monthPickerView.isCircular = value
            yearPickerView.isCircular = value
        }

    private val yearAdapter = YearWheelAdapter(WeakReference(this))
    private val monthAdapter = DateWheelAdapter(WeakReference(this))
    private val dayAdapter = DateWheelAdapter(WeakReference(this))

    private fun setYear(year: Int, animated: Boolean) {
        if (this.year == year) {
            return
        }
        yearPickerView.setSelectedIndex(year, animated)
    }

    private fun setMonth(month: Int, animated: Boolean) {
        if (this.month == month) {
            return
        }
        monthPickerView.setSelectedIndex(month, animated)
    }

    private fun setDay(day: Int, animated: Boolean) {
        if (this.day == day) {
            return
        }
        dayPickerView.setSelectedIndex(day - 1, animated)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.day_time_picker_view, this, true)
        dayPickerView = findViewById(R.id.right_picker)
        dayPickerView.setAdapter(dayAdapter)
        monthPickerView = findViewById(R.id.mid_picker)
        monthPickerView.setAdapter(monthAdapter)
        monthAdapter.values = (0 until 12).map {
            TextWheelPickerView.Item(
                "$it",
                DateFormatSymbols.getInstance().months[it]
            )
        }
        yearPickerView = findViewById(R.id.left_picker)
        yearPickerView.setAdapter(yearAdapter)
        addView(highlightView)
        (highlightView.layoutParams as? LayoutParams)?.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height =
                context.resources.getDimensionPixelSize(R.dimen.text_wheel_picker_item_height)
            gravity = Gravity.CENTER_VERTICAL
        }

        dayPickerView.setWheelListener(this)
        monthPickerView.setWheelListener(this)
        yearPickerView.setWheelListener(this)

        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun updateDayPickerViewIfNeeded(): Boolean {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        if (dayAdapter.values.count() == dayCount) {
            return false
        }
        dayAdapter.values = (0 until dayCount).map {
            val day = it + 1
            TextWheelPickerView.Item(
                "$day",
                context.getString(R.string.day_time_picker_format_day, day)
            )
        }
        dayPickerView.post {
            dayPickerView.refreshCurrentPosition()
        }
        return true
    }

    private fun updateDateIfNeeded(): Boolean {
        minDate?.let {
            if (updateDateByMinDate(minDateCalendar)) {
                return true
            }
        }
        maxDate?.let {
            if (updateDateByMaxDate(maxDateCalendar)) {
                return true
            }
        }
        return false
    }

    private fun updateDateByMinDate(calendar: Calendar): Boolean {
        var changed = false
        val minYear = calendar.get(Calendar.YEAR)
        if (year > minYear) {
            return changed
        }
        if (year < minYear) {
            setYear(minYear, true)
            changed = true
        }

        val minMonth = calendar.get(Calendar.MONTH)
        if (month > minMonth) {
            return changed
        }
        if (month < minMonth) {
            setMonth(minMonth, true)
            changed = true
        }

        val minDay = calendar.get(Calendar.DAY_OF_MONTH)
        if (day < minDay) {
            setDay(minDay, true)
            changed = true
        }
        return changed
    }

    private fun updateDateByMaxDate(calendar: Calendar): Boolean {
        var changed = false
        val maxYear = calendar.get(Calendar.YEAR)
        if (year < maxYear) {
            return changed
        }
        if (year > maxYear) {
            setYear(maxYear, true)
            changed = true
        }

        val maxMonth = calendar.get(Calendar.MONTH)
        if (month < maxMonth) {
            return changed
        }
        if (month > maxMonth) {
            setMonth(maxMonth, true)
            changed = true
        }

        val maxDay = calendar.get(Calendar.DAY_OF_MONTH)
        if (day > maxDay) {
            setDay(maxDay, true)
            changed = true
        }
        return changed
    }

    private fun dateIsValid(year: Int, month: Int, day: Int): Boolean {
        if (year == NO_POSITION && month == NO_POSITION && day == NO_POSITION) {
            return false
        }
        val format = "%04d%02d%02d"
        val selectedDateString = format.format(year, month, day)
        minDateData()?.let {
            if (selectedDateString < format.format(it.year, it.month, it.day)) {
                return false
            }
        }
        maxDateData()?.let {
            if (selectedDateString > format.format(it.year, it.month, it.day)) {
                return false
            }
        }
        return true
    }

    private var isCurrentYearOnEdge: Boolean? = null
        set(value) {
            field = value
            if (value == false) {
                isCurrentMonthOnEdge = false
            }
        }
    private var isCurrentMonthOnEdge: Boolean? = null

    // region BaseWheelPickerView.WheelPickerViewListener
    override fun didSelectItem(picker: BaseWheelPickerView, index: Int) {
        var dayPickerUpdated = false
        if (picker == yearPickerView || picker == monthPickerView) {
            dayPickerUpdated = updateDayPickerViewIfNeeded()
        }
        if (minDate != null || maxDate != null) {
            val isCurrentYearOnEdge =
                minDateCalendar.get(Calendar.YEAR) == year || maxDateCalendar.get(Calendar.YEAR) == year
            if (this.isCurrentYearOnEdge != isCurrentYearOnEdge) {
                this.isCurrentYearOnEdge = isCurrentYearOnEdge
                if (picker == yearPickerView) {
                    monthAdapter.notifyDataSetChanged()
                }
                val isCurrentMonthOnEdge =
                    minDateCalendar.get(Calendar.MONTH) == month || maxDateCalendar.get(Calendar.MONTH) == month
                if (this.isCurrentMonthOnEdge != isCurrentMonthOnEdge && !dayPickerUpdated) {
                    this.isCurrentMonthOnEdge = isCurrentMonthOnEdge
                    dayAdapter.notifyDataSetChanged()
                }
            }
        }
        if (!dateIsValid(year, month, day)) {
            return
        }
        listener?.didSelectData(year, month, day)
    }

    override fun onScrollStateChanged(state: Int) {
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            updateDateIfNeeded()
        }
    }
    // endregion

    // region DateWheelAdapter.ValueEnabledProvider
    override fun isEnabled(adapter: DateWheelAdapter, valueIndex: Int): Boolean {
        when (adapter) {
            yearAdapter -> {
                minDateData()?.year?.let {
                    if (valueIndex < it) {
                        return false
                    }
                }
                maxDateData()?.year?.let {
                    if (valueIndex > it) {
                        return false
                    }
                }
            }
            monthAdapter -> {
                minDateData()?.let {
                    if (year > it.year) {
                        return true
                    }
                    if (valueIndex < it.month) {
                        return false
                    }
                }
                maxDateData()?.let {
                    if (year < it.year) {
                        return true
                    }
                    if (valueIndex > it.month) {
                        return false
                    }
                }
            }
            dayAdapter -> {
                val day = valueIndex + 1
                minDateData()?.let {
                    if (year > it.year || month > it.month) {
                        return true
                    }
                    if (day < it.day) {
                        return false
                    }
                }
                maxDateData()?.let {
                    if (year < it.year || month < it.month) {
                        return true
                    }
                    if (day > it.day) {
                        return false
                    }
                }
            }
        }
        return true
    }
    // endregion
}