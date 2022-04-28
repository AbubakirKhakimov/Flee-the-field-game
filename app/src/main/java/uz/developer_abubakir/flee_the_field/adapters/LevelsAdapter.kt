package uz.developer_abubakir.flee_the_field.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.developer_abubakir.flee_the_field.R
import uz.developer_abubakir.flee_the_field.databinding.LevelsItemLayoutBinding

interface LevelsAdapterCallBack{
    fun itemSelectedListener(level: Int)
}

class LevelsAdapter(val maxLevelSize: Int, val currentLevel: Int, val context: Context, val levelsAdapterCallBack: LevelsAdapterCallBack)
    :RecyclerView.Adapter<LevelsAdapter.ItemHolder>() {
    inner class ItemHolder(val binding: LevelsItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(LevelsItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

        holder.binding.box.setCardBackgroundColor(
            when {
                position+1 < currentLevel -> {
                    Color.parseColor("#BA4E4E")
                }
                position+1 == currentLevel -> {
                    ContextCompat.getColor(context, R.color.red)
                }
                else -> {
                    Color.parseColor("#4A4A4A")
                }
            }
        )

        if (position == maxLevelSize){
            holder.binding.lockImage.visibility = View.VISIBLE
            holder.binding.levelNumber.visibility = View.GONE
        }else{
            holder.binding.levelNumber.text = "${position+1}"
        }

        holder.binding.root.setOnClickListener{
            if (holder.binding.levelNumber.visibility == View.VISIBLE)
            levelsAdapterCallBack.itemSelectedListener(holder.binding.levelNumber.text.toString().toInt())
        }
    }

    override fun getItemCount(): Int {
        return maxLevelSize+1
    }
}