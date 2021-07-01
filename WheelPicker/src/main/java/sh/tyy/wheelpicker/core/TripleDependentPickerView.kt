package sh.tyy.wheelpicker.core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sh.tyy.wheelpicker.R
import java.lang.ref.WeakReference

typealias TripleDependentData = Triple<Int, Int, Int>

open class ItemEnableWheelAdapter(
    protected val valueEnabledProvider: WeakReference<ValueEnabledProvider>
) :
    BaseWheelPickerView.Adapter<TextWheelPickerView.Item, TextWheelViewHolder>() {
    interface ValueEnabledProvider {
        fun isEnabled(adapter: RecyclerView.Adapter<*>, valueIndex: Int): Boolean
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

/**
 * 3-column selector with cascading dependencies
 *
 * first column <- second column <- third column
 *
 * e.g. the year-month-day selector. Day depends on month, month depends on year.
 *
 * @property adapters Triple<Adapter<*>, Adapter<*>, Adapter<*>> adapters
 * @property currentData Triple<Int, Int, Int> current data
 * @constructor
 */
abstract class TripleDependentPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ItemEnableWheelAdapter.ValueEnabledProvider {

    protected abstract val adapters: Triple<RecyclerView.Adapter<*>, RecyclerView.Adapter<*>, RecyclerView.Adapter<*>>

    abstract val currentData: TripleDependentData

    protected abstract fun minData(): TripleDependentData?
    protected abstract fun maxData(): TripleDependentData?

    abstract fun value(adapter: RecyclerView.Adapter<*>, valueIndex: Int): Int

    protected fun reloadPickersIfNeeded(
        oldData: TripleDependentData?,
        newData: TripleDependentData?
    ) {
        if (oldData?.first != newData?.first) {
            adapters.first.notifyDataSetChanged()
        }
        val current = currentData
        if (current.first == newData?.first &&
            (!(newData.first == oldData?.first && newData.second == oldData.second))
        ) {
            adapters.second.notifyDataSetChanged()
        }
        if (current.first == newData?.first && current.second == newData.second && newData != oldData
        ) {
            adapters.third.notifyDataSetChanged()
        }
    }

    protected abstract fun setFirst(value: Int, animated: Boolean)

    protected abstract fun setSecond(value: Int, animated: Boolean)

    protected abstract fun setThird(value: Int, animated: Boolean)

    protected fun updateCurrentDataByDataRangeIfNeeded(): Boolean {
        minData()?.let {
            if (updateCurrentDataByMinData(it)) {
                return true
            }
        }
        maxData()?.let {
            if (updateCurrentDataByMaxData(it)) {
                return true
            }
        }
        return false
    }

    protected fun updateCurrentDataByMinData(minData: TripleDependentData): Boolean {
        var changed = false
        val current = currentData
        if (current.first > minData.first) {
            return changed
        }
        if (current.first < minData.first) {
            setFirst(minData.first, true)
            changed = true
        }

        if (current.second > minData.second) {
            return changed
        }
        if (current.second < minData.second) {
            setSecond(minData.second, true)
            changed = true
        }

        if (current.third < minData.third) {
            setThird(minData.third, true)
            changed = true
        }
        return changed
    }

    protected fun updateCurrentDataByMaxData(maxData: TripleDependentData): Boolean {
        var changed = false
        val current = currentData
        if (current.first < maxData.first) {
            return changed
        }
        if (current.first > maxData.first) {
            setFirst(maxData.first, true)
            changed = true
        }

        if (current.second < maxData.second) {
            return changed
        }
        if (current.second > maxData.second) {
            setSecond(maxData.second, true)
            changed = true
        }

        if (current.third > maxData.third) {
            setThird(maxData.third, true)
            changed = true
        }
        return changed
    }

    // region ItemEnableWheelAdapter.ValueEnabledProvider
    override fun isEnabled(adapter: RecyclerView.Adapter<*>, valueIndex: Int): Boolean {
        val adapters = this.adapters
        val value = value(adapter, valueIndex)
        when (adapter) {
            adapters.first -> {
                minData()?.first?.let {
                    if (value < it) {
                        return false
                    }
                }
                maxData()?.first?.let {
                    if (value > it) {
                        return false
                    }
                }
            }
            adapters.second -> {
                val current = currentData
                minData()?.let {
                    if (current.first > it.first) {
                        return true
                    }
                    if (value < it.second) {
                        return false
                    }
                }
                maxData()?.let {
                    if (current.first < it.first) {
                        return true
                    }
                    if (value > it.second) {
                        return false
                    }
                }
            }
            adapters.third -> {
                val current = currentData
                minData()?.let {
                    if (current.first > it.first || current.second > it.second) {
                        return true
                    }
                    if (value < it.third) {
                        return false
                    }
                }
                maxData()?.let {
                    if (current.first < it.first || current.second < it.second) {
                        return true
                    }
                    if (value > it.third) {
                        return false
                    }
                }
            }
        }
        return true
    }
    // endregion
}