package uz.developer_abubakir.flee_the_field.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.developer_abubakir.flee_the_field.R
import uz.developer_abubakir.flee_the_field.databinding.GameZoneItemLayoutBinding
import kotlin.random.Random.Default.nextInt

class GameZoneAdapter(val activeZoneList: ArrayList<Boolean>, val context: Context):RecyclerView.Adapter<GameZoneAdapter.ItemHolder>() {
    inner class ItemHolder(val binding: GameZoneItemLayoutBinding): RecyclerView.ViewHolder(binding.root)

    var playerPosition = 59
    var refereePosition = 17

    var flagsList = ArrayList<Int>()

    init {
        addFlagRandomPosition()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(GameZoneItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (activeZoneList[position]){
            holder.binding.zone.setCardBackgroundColor(ContextCompat.getColor(context, R.color.game_zone_active))
        }else{
            holder.binding.zone.setCardBackgroundColor(ContextCompat.getColor(context, R.color.game_zone_no_active))
        }

        when {
            refereePosition == position -> {
                holder.binding.image.visibility = View.VISIBLE
                holder.binding.image.setImageResource(R.drawable.judging_icon)
            }
            playerPosition == position -> {
                holder.binding.image.visibility = View.VISIBLE
                holder.binding.image.setImageResource(R.drawable.player_icon)
            }
            else -> {
                holder.binding.image.visibility = View.GONE
            }
        }

        if (3 == position && holder.binding.image.visibility == View.GONE){
            holder.binding.image.visibility = View.VISIBLE
            holder.binding.image.setImageResource(R.drawable.ic_arrow_finish)
        }

        if (flagsList.contains(position) && holder.binding.image.visibility == View.GONE){
            holder.binding.image.visibility = View.VISIBLE
            holder.binding.image.setImageResource(R.drawable.flag)
        }
    }

    override fun getItemCount(): Int {
        return 63
    }

    private fun addFlagRandomPosition(){
        repeat(nextInt(1, 4)){
            var position = nextInt(63)

            while (!activeZoneList[position] || position == playerPosition || position == refereePosition || position == 3){
                position = nextInt(63)
            }

            flagsList.add(position)
        }
    }

}