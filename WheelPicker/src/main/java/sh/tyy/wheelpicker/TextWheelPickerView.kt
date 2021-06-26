package sh.tyy.wheelpicker

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Outline
import android.util.AttributeSet
import android.view.*
import android.widget.TextView

class TextWheelViewHolder(private val textView: TextView) :
    BaseWheelPickerView.ViewHolder<TextWheelPickerView.Item>(textView) {
    override fun onBindData(data: TextWheelPickerView.Item) {
        textView.text = data.text
    }
}

class TextWheelAdapter :
    BaseWheelPickerView.Adapter<TextWheelPickerView.Item, TextWheelViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextWheelViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.wheel_picker_item, parent, false) as TextView
        return TextWheelViewHolder(view)
    }
}

class TextWheelPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseWheelPickerView(context, attrs, defStyleAttr) {
    data class Item(val id: String, val text: String)

    private val highlightView: View = Utils.buildHighlightView(context)

    val adapter: TextWheelAdapter = TextWheelAdapter()

    var isHighlightingVisible: Boolean
        set(value) {
            highlightView.visibility = if (value) View.VISIBLE else View.INVISIBLE
        }
        get() {
            return highlightView.visibility == View.VISIBLE
        }

    override fun setHapticFeedbackEnabled(hapticFeedbackEnabled: Boolean) {
        super.setHapticFeedbackEnabled(hapticFeedbackEnabled)
        recyclerView.isHapticFeedbackEnabled = hapticFeedbackEnabled
    }

    init {
        setAdapter(adapter)
        addView(highlightView)
        (highlightView.layoutParams as? LayoutParams)?.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height =
                context.resources.getDimensionPixelSize(R.dimen.text_wheel_item_height)
            gravity = Gravity.CENTER_VERTICAL
        }

        attrs?.let {
            context.theme.obtainStyledAttributes(
                it,
                R.styleable.TextWheelPickerView,
                defStyleAttr,
                0
            ).apply {
                for (i in 0 until indexCount) {
                    when (getIndex(i)) {
                        R.styleable.TextWheelPickerView_highlighting_visible -> {
                            isHighlightingVisible = getBoolean(
                                R.styleable.TextWheelPickerView_highlighting_visible,
                                true
                            )
                        }
                    }
                }
            }
        }
    }
}
