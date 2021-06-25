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

    private val highlightView: View = View(context)

    val adapter: TextWheelAdapter = TextWheelAdapter()

    init {
        setAdapter(adapter)
        addView(highlightView)
        highlightView.setBackgroundColor(Color.parseColor("#11000000"))
        highlightView.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(
                    0,
                    0,
                    view.width,
                    view.height,
                    dpToPx(8).toFloat()
                )
            }
        }
        highlightView.clipToOutline = true
        (highlightView.layoutParams as? LayoutParams)?.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height =
                context.resources.getDimensionPixelSize(R.dimen.text_wheel_item_height)
            gravity = Gravity.CENTER_VERTICAL
        }
    }
}

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}