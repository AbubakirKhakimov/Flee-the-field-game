package uz.developer_abubakir.flee_the_field.utils

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import uz.developer_abubakir.flee_the_field.R

interface CustomSwitchCallBack{
    fun switchStateChangedListener(state: Boolean)
}

class CustomSwitchController(
    val context: Context,
    val case: CardView,
    val switch: CardView,
    var switchState: Boolean,
    val customSwitchCallBack: CustomSwitchCallBack) {

    init {
        setState()

        case.setOnClickListener {
            switchState = !switchState
            setState()
            customSwitchCallBack.switchStateChangedListener(switchState)
        }
    }

    private fun setState(){
        setSwitchPosition()
        changeColors()
    }

    private fun setSwitchPosition(){
        switch.layoutParams = getParams()
    }

    private fun changeColors(){
        if (switchState){
            case.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red))
            switch.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }else{
            case.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
            switch.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red))
        }
    }

    private fun getParams(): ViewGroup.LayoutParams{
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = if (switchState) Gravity.END else Gravity.START
        return params
    }

}