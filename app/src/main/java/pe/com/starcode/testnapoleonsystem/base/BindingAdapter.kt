package pe.com.starcode.testnapoleonsystem.base

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun View.visible(isVisible: Boolean?) {
    visibility = if (isVisible == true) View.VISIBLE else View.GONE
}
