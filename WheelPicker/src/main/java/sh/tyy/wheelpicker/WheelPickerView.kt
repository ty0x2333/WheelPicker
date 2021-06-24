package sh.tyy.wheelpicker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WheelPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var recyclerView: RecyclerView
    private val adapter: Adapter

    var isCircular: Boolean = false
        set(value) {
            field = value
            adapter.notifyDataSetChanged()
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.wheel_picker_view, this, true)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = Adapter()
        recyclerView.adapter = adapter
    }

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    private inner class Adapter : RecyclerView.Adapter<ViewHolder>() {
        private var values: List<String> = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.wheel_picker_cell, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            (holder.itemView as? TextView)?.text = values.getOrNull(position)
        }

        override fun getItemCount(): Int {
            return if (isCircular) Int.MAX_VALUE else values.count()
        }
    }
}