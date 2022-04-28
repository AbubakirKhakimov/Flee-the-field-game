package uz.developer_abubakir.flee_the_field.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.orhanobut.hawk.Hawk
import uz.developer_abubakir.flee_the_field.R
import uz.developer_abubakir.flee_the_field.databinding.FragmentMainMenuBinding
import uz.developer_abubakir.flee_the_field.models.GamePublicDatas
import java.text.SimpleDateFormat
import java.util.*

class MainMenuFragment : Fragment() {

    lateinit var binding: FragmentMainMenuBinding
    val settings = GamePublicDatas.settings!!
    var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!GamePublicDatas.isMusicPlayed()){
            GamePublicDatas.playMusicByRaw(requireActivity(), R.raw.menu)
        }

        checkTimer()

        binding.getBonus.setOnClickListener {
            getBonus()
        }

        binding.start.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_gameFragment)
        }

        binding.levels.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_levelsFragment)
        }

        binding.settings.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_settingsFragment)
        }

    }

    private fun getBonus(){
        if (settings.bonusCount < 3){
            settings.bonusCount++
        }
        settings.getLastBonusTime = Date().time
        GamePublicDatas.write()

        findNavController().navigate(R.id.action_mainMenuFragment_to_bonusFragment)
    }

    private fun checkTimer(){
        val differenceTime = 86400000 - (Date().time - settings.getLastBonusTime) - 21600000

        if (differenceTime <= 0){
            changeBonusUI(true)
        }else{
            changeBonusUI(false)
            preparationTimer(differenceTime)
        }
    }

    private fun changeBonusUI(isClickable: Boolean){
        if (isClickable){
            binding.getBonus.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
            binding.timer.visibility = View.GONE
            binding.getBonus.isEnabled = true
        }else{
            binding.getBonus.setBackgroundColor(Color.parseColor("#80F83636"))
            binding.timer.visibility = View.VISIBLE
            binding.getBonus.isEnabled = false
        }
    }

    private fun preparationTimer(differenceTime: Long){
        timer = object :CountDownTimer(differenceTime, 10000){
            override fun onTick(millisUntilFinished: Long) {
                binding.timer.text = getStrTime(millisUntilFinished)
            }

            override fun onFinish() {
                changeBonusUI(true)
                timer = null
            }
        }
    }

    fun getStrTime(it :Long):String{
        return SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(it))
    }

    override fun onStop() {
        super.onStop()
        if (timer != null) {
            timer!!.cancel()
        }
    }

    override fun onStart() {
        super.onStart()
        if (timer != null) {
            timer!!.start()
        }
    }

}