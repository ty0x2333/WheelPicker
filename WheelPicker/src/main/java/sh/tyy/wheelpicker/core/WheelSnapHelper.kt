package sh.tyy.wheelpicker.core

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView


internal class WheelSnapHelper : LinearSnapHelper() {
    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        return (layoutManager as? LinearLayoutManager)?.takeIf { isValidSnap(it) }
            ?.run { super.findSnapView(layoutManager) }
    }

    private fun isValidSnap(linearLayoutManager: LinearLayoutManager): Boolean {
        val firstCompletelyVisibleItemPosition =
            linearLayoutManager.findFirstCompletelyVisibleItemPosition()
        val lastCompletelyVisibleItemPosition =
            linearLayoutManager.findLastCompletelyVisibleItemPosition()
        return firstCompletelyVisibleItemPosition != 0 && lastCompletelyVisibleItemPosition != linearLayoutManager.itemCount - 1
    }
}
