package sh.tyy.wheelpicker.example.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import sh.tyy.wheelpicker.BaseWheelPickerView
import sh.tyy.wheelpicker.example.R

class CustomWheelViewHolder(itemView: View) :
    BaseWheelPickerView.ViewHolder<CustomWheelPickerView.Item>(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.text_view)
    private val imageView: ImageView = itemView.findViewById(R.id.icon_image_ivew)
    override fun onBindData(data: CustomWheelPickerView.Item) {
        textView.text = data.text
        imageView.setImageDrawable(data.icon)
    }
}

class CustomWheelAdapter :
    BaseWheelPickerView.Adapter<CustomWheelPickerView.Item, CustomWheelViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomWheelViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_wheel_picker_item, parent, false)
        return CustomWheelViewHolder(view)
    }
}

class CustomWheelPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseWheelPickerView(context, attrs, defStyleAttr) {
    data class Item(val id: String, val icon: Drawable?, val text: String)

    private val highlightView: View = run {
        val view = View(context)
        view.background = ContextCompat.getDrawable(context, R.drawable.custom_wheel_highlight_bg)
        view
    }

    val adapter: CustomWheelAdapter = CustomWheelAdapter()

    init {
        setAdapter(adapter)
        addView(highlightView)
        (highlightView.layoutParams as? LayoutParams)?.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height =
                context.resources.getDimensionPixelSize(R.dimen.custom_wheel_picker_item_height)
            gravity = Gravity.CENTER_VERTICAL
        }
    }
}