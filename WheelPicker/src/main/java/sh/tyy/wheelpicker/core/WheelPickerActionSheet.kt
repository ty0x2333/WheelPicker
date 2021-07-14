package sh.tyy.wheelpicker.core

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.view.*
import android.widget.PopupWindow
import sh.tyy.wheelpicker.R
import sh.tyy.wheelpicker.databinding.PickerActionSheetContentBinding

abstract class WheelPickerActionSheet<Picker : View>(context: Context) {
    var pickerView: Picker? = null
        private set
    protected val popupWindow: PopupWindow
    protected val contentBinding: PickerActionSheetContentBinding =
        PickerActionSheetContentBinding.inflate(LayoutInflater.from(context), null, false)

    fun setOnClickOkButtonListener(listener: View.OnClickListener) {
        contentBinding.okButton.setOnClickListener(listener)
    }

    fun setOnDismissListener(listener: PopupWindow.OnDismissListener) {
        popupWindow.setOnDismissListener(listener)
    }

    init {
        popupWindow = PopupWindow(
            contentBinding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.animationStyle = R.style.WheelSheetTranslate
        popupWindow.setBackgroundDrawable(BitmapDrawable())
    }

    protected fun setPickerView(pickerView: Picker) {
        this.pickerView = pickerView
        val containerView = contentBinding.containerView
        containerView.addView(pickerView)
        pickerView.layoutParams.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    fun show(window: Window) {
        val rect = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rect)
        val windowHeight = window.decorView.height
        popupWindow.apply {
            showAtLocation(window.decorView.rootView, Gravity.BOTTOM, 0, windowHeight - rect.bottom)
            contentView.rootView?.let { container ->
                val wm =
                    contentView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                (container.layoutParams as? WindowManager.LayoutParams)?.let { params ->
                    params.flags = params.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
                    params.dimAmount = 0.4f
                    wm.updateViewLayout(container, params)
                }
            }
        }
    }

    fun hide() {
        popupWindow.dismiss()
    }
}
