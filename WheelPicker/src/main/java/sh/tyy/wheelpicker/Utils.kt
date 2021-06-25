package sh.tyy.wheelpicker

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider


object Utils {
    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

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
                    dpToPx(8).toFloat()
                )
            }
        }
        highlightView.clipToOutline = true
        return highlightView
    }
}