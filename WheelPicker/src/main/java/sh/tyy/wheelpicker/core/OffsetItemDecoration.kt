package sh.tyy.wheelpicker.core

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

internal class OffsetItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val offset = (parent.height - view.layoutParams.height) / 2
        if (parent.getChildAdapterPosition(view) == 0) {
            (view.layoutParams as ViewGroup.MarginLayoutParams).topMargin = 0
            outRect.top = offset
        } else if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
            (view.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = 0
            outRect.bottom = offset
        }
    }
}