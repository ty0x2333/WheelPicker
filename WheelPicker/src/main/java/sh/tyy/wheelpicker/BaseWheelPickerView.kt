package sh.tyy.wheelpicker

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView

abstract class BaseWheelPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    WheelPickerRecyclerView.WheelPickerRecyclerViewListener {

    interface AdapterImp {
        var isCircular: Boolean
        val valueCount: Int
    }

    interface WheelPickerViewListener {
        fun didSelectItem(picker: BaseWheelPickerView, index: Int)
        fun onScrollStateChanged(state: Int) {}
    }

    abstract class ViewHolder<Element : Any>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun onBindData(data: Element)
    }

    abstract class Adapter<Element : Any, ViewHolder : BaseWheelPickerView.ViewHolder<Element>> :
        RecyclerView.Adapter<ViewHolder>(), AdapterImp {
        open var values: List<Element> = emptyList()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override val valueCount: Int
            get() = values.count()

        override var isCircular: Boolean = false
            set(value) {
                if (field == value) {
                    return
                }
                field = value
                notifyDataSetChanged()
            }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val value =
                values.getOrNull(if (isCircular) position % values.count() else position) ?: return
            holder.onBindData(value)
        }

        override fun getItemCount(): Int {
            return if (isCircular) Int.MAX_VALUE else values.count()
        }
    }

    var selectedIndex: Int
        set(value) {
            setSelectedIndex(value, false)
        }
        get() {
            val position = recyclerView.currentPosition
            return if (isCircular) {
                position % ((recyclerView.adapter as? AdapterImp)?.valueCount ?: 0)
            } else {
                position
            }
        }

    fun setSelectedIndex(index: Int, animated: Boolean) {
        val dstPosition: Int = if (isCircular) {
            index - selectedIndex + recyclerView.currentPosition
        } else {
            index
        }
        if (animated) {
            recyclerView.smoothScrollToCenterPosition(dstPosition)
        } else {
            recyclerView.scrollToCenterPosition(dstPosition, true)
        }
    }

    protected val recyclerView: WheelPickerRecyclerView by lazy {
        val recyclerView = WheelPickerRecyclerView(context)
        addView(recyclerView)
        recyclerView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        recyclerView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        recyclerView.setWheelListener(this)
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                listener?.onScrollStateChanged(newState)
            }
        })
        recyclerView
    }

    fun <Element : Any, ViewHolder : BaseWheelPickerView.ViewHolder<Element>> setAdapter(adapter: Adapter<Element, ViewHolder>) {
        recyclerView.adapter = adapter
    }

    private var listener: WheelPickerViewListener? = null

    fun setWheelListener(listener: WheelPickerViewListener) {
        this.listener = listener
    }

    override fun setHapticFeedbackEnabled(hapticFeedbackEnabled: Boolean) {
        super.setHapticFeedbackEnabled(hapticFeedbackEnabled)
        recyclerView.isHapticFeedbackEnabled = hapticFeedbackEnabled
    }

    fun refreshCurrentPosition() {
        recyclerView.refreshCurrentPosition()
    }

    var isCircular: Boolean
        set(value) {
            val selectedIndex = this.selectedIndex
            (recyclerView.adapter as? AdapterImp)?.isCircular = value
            val completion = {
                recyclerView.refreshCurrentPosition()
            }
            if (value) {
                val valueCount = (recyclerView.adapter as? AdapterImp)?.valueCount ?: 0
                if (valueCount > 0) {
                    recyclerView.scrollToCenterPosition(
                        ((Int.MAX_VALUE / 2) / valueCount) * valueCount + selectedIndex,
                        true,
                        completion
                    )
                } else {
                    recyclerView.scrollToCenterPosition(selectedIndex, true, completion)
                }
            } else {
                recyclerView.scrollToCenterPosition(selectedIndex, true, completion)
            }
        }
        get() = (recyclerView.adapter as? AdapterImp)?.isCircular ?: false

    init {
        recyclerView
    }

    // region WheelPickerRecyclerView.WheelPickerRecyclerViewListener
    override fun didSelectItem(position: Int) {
        listener?.didSelectItem(this, selectedIndex)
    }
    // enregion
}