package sh.tyy.wheelpicker

import android.content.Context
import android.graphics.Color
import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider


object Utils {
    fun buildHighlightView(context: Context): View {
        val highlightView: View = View(context)
        highlightView.setBackgroundColor(Color.parseColor("#11000000"))
        highlightView.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(
                    0,
                    0,
                    view.width,
                    view.height,
                    context.resources.getDimensionPixelOffset(R.dimen.wheel_picker_highlight_radius)
                        .toFloat()
                )
            }
        }
        highlightView.clipToOutline = true
        return highlightView
    }
}