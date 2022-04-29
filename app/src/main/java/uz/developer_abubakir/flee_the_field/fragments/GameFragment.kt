package uz.developer_abubakir.flee_the_field.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.developer_abubakir.flee_the_field.R
import uz.developer_abubakir.flee_the_field.adapters.GameZoneAdapter
import uz.developer_abubakir.flee_the_field.databinding.CatchedDialogLayoutBinding
import uz.developer_abubakir.flee_the_field.databinding.FragmentGameBinding
import uz.developer_abubakir.flee_the_field.databinding.WinDialogLayoutBinding
import uz.developer_abubakir.flee_the_field.models.GamePublicDatas
import uz.developer_abubakir.flee_the_field.utils.OnSwipeTouchListener
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.random.Random.Default.nextInt


class GameFragment : Fragment() {

    lateinit var binding: FragmentGameBinding
    lateinit var gameZoneAdapter: GameZoneAdapter
    val vibrator: Vibrator by lazy {
        requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    var replay = false

    var level = 1
    var score = 0
    var moveCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window?.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.blue)
        level = arguments?.getInt("selectedLevel", GamePublicDatas.settings!!.level) ?: GamePublicDatas.settings!!.level

        GamePublicDatas.playMusicByRaw(requireActivity(), R.raw.game)
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window?.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.red)

        if (!replay) {
            GamePublicDatas.playMusicByRaw(requireActivity(), R.raw.menu)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGameBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateBonus(false)
        initSwipeListeners()

        gameZoneAdapter = GameZoneAdapter(getActiveZoneList(), requireActivity())
        binding.gameZoneRv.adapter = gameZoneAdapter

        binding.oneWhistle.setOnClickListener {
            useBonus()
        }

        binding.twoWhistle.setOnClickListener {
            useBonus()
        }

        binding.threeWhistle.setOnClickListener {
            useBonus()
        }

    }

    private fun useBonus(){
        updateBonus(true)
        moveCount = 2
    }

    private fun getActiveZoneList():ArrayList<Boolean>{
        val list = ArrayList<Boolean>()

        for (i in 0 until 63){
            if (i == 0 ||i == 1 || i == 5 || i == 6 ||
                i == 7 || i == 13 || i == 49|| i == 55 ||
                i == 56 || i == 57 || i == 61 || i == 62){
                list.add(false)
            }else {
                list.add(true)
            }
        }

        if (level >= 2) {
            repeat(5) {
                var randomPosition = nextInt(63)

                while (randomPosition == 3 ||
                    randomPosition == 59 ||
                    randomPosition == 17
                ) {
                    randomPosition = nextInt(63)
                }

                list[randomPosition] = false
            }
        }

        return list
    }

    private fun updateScore(add: Boolean){
        if (add) {
            score++
        }
        binding.score.text = score.toString()
    }

    private fun updateBonus(inc: Boolean){
        GamePublicDatas.settings!!.apply {
            if (inc) {
                bonusCount--
                GamePublicDatas.write()
            }

            when(bonusCount){
                0 -> {
                    binding.oneWhistle.visibility = View.GONE
                    binding.twoWhistle.visibility = View.GONE
                    binding.threeWhistle.visibility = View.GONE
                }
                1 -> {
                    binding.oneWhistle.visibility = View.VISIBLE
                    binding.twoWhistle.visibility = View.GONE
                    binding.threeWhistle.visibility = View.GONE
                }
                2 -> {
                    binding.oneWhistle.visibility = View.VISIBLE
                    binding.twoWhistle.visibility = View.VISIBLE
                    binding.threeWhistle.visibility = View.GONE
                }
                3 -> {
                    binding.oneWhistle.visibility = View.VISIBLE
                    binding.twoWhistle.visibility = View.VISIBLE
                    binding.threeWhistle.visibility = View.VISIBLE
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initSwipeListeners(){
        binding.root.setOnTouchListener(object :OnSwipeTouchListener(requireActivity()){
            override fun onSwipeBottom() {
                super.onSwipeBottom()
                changePlayerPosition(3)
            }

            override fun onSwipeLeft() {
                super.onSwipeLeft()
                changePlayerPosition(0)
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                changePlayerPosition(1)
            }

            override fun onSwipeTop() {
                super.onSwipeTop()
                changePlayerPosition(2)
            }
        })

        binding.gameZoneRv.setOnTouchListener(object :OnSwipeTouchListener(requireActivity()){
            override fun onSwipeBottom() {
                super.onSwipeBottom()
                changePlayerPosition(3)
            }

            override fun onSwipeLeft() {
                super.onSwipeLeft()
                changePlayerPosition(0)
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                changePlayerPosition(1)
            }

            override fun onSwipeTop() {
                super.onSwipeTop()
                changePlayerPosition(2)
            }
        })
    }

    private fun changeBotPosition(){
        gameZoneAdapter.apply {
            if (refereePosition == playerPosition) {
                notifyDataSetChanged()
                showCatchedDialog()
                return
            }

            val left = refereePosition - 1
            val right = refereePosition + 1
            val top = refereePosition - 7
            val bottom = refereePosition + 7

            val leftDifference = abs(playerPosition - left)
            val rightDifference = abs(playerPosition - right)
            val topDifference = abs(playerPosition - top)
            val bottomDifference = abs(playerPosition - bottom)

            val differencesList = arrayListOf<Int>(
                leftDifference,
                rightDifference,
                topDifference,
                bottomDifference
            )
            differencesList.sort()

            for(item in differencesList){
                val position = when(item){
                    leftDifference -> left
                    rightDifference -> right
                    topDifference -> top
                    bottomDifference -> bottom
                    else -> refereePosition
                }

                if (position in 0..62 && activeZoneList[position]){
                    refereePosition = position
                    break
                }
            }

            moveCount++

            when {
                refereePosition == playerPosition -> {
                    showCatchedDialog()
                }
                playerPosition == 3 -> {
                    showWinDialog()
                }
                flagsList.contains(playerPosition) -> {
                    score = if (score >= 5) score - 5 else 0
                    updateScore(false)
                    flagsList.remove(playerPosition)
                }
            }

            notifyDataSetChanged()
        }
    }

    fun changePlayerPosition(swipeSide: Int){
        gameZoneAdapter.apply {
            val position = when(swipeSide){
                0 -> playerPosition - 1  //left
                1 -> playerPosition + 1  //right
                2 -> playerPosition - 7  //top
                3 -> playerPosition + 7  //bottom
                else -> playerPosition
            }

            if (position in 0..62 && activeZoneList[position]){
                playerPosition = position
                moveCount--
                updateScore(true)

                if (moveCount == 0) {
                    changeBotPosition()
                }else{
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun showCatchedDialog(){
        val customDialog = AlertDialog.Builder(requireActivity()).create()
        val dialogBinding = CatchedDialogLayoutBinding.inflate(layoutInflater)
        customDialog.setView(dialogBinding.root)
        customDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        customDialog.setCancelable(false)

        if (GamePublicDatas.settings!!.vibration) {
            vibrate()
        }
        GamePublicDatas.playMusicByRaw(requireActivity(), R.raw.clash_with_the_referee)

        dialogBinding.replay.setOnClickListener {
            replay = true
            findNavController().popBackStack(R.id.gameFragment, true)
            findNavController().navigate(R.id.gameFragment, bundleOf(
                "selectedLevel" to level
            ))
            customDialog.dismiss()
        }

        dialogBinding.levels.setOnClickListener {
            findNavController().navigate(R.id.action_gameFragment_to_levelsFragment)
            customDialog.dismiss()
        }

        dialogBinding.menu.setOnClickListener {
            findNavController().navigate(R.id.action_gameFragment_to_mainMenuFragment)
            customDialog.dismiss()
        }

        customDialog.show()
    }

    private fun showWinDialog(){
        val customDialog = AlertDialog.Builder(requireActivity()).create()
        val dialogBinding = WinDialogLayoutBinding.inflate(layoutInflater)
        customDialog.setView(dialogBinding.root)
        customDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        customDialog.setCancelable(false)

        GamePublicDatas.apply {
            if (settings!!.level < 3 && settings!!.level == level){
                settings!!.level++
                write()
            }
        }

        dialogBinding.score.text = "${getString(R.string.score)}: $score"

        if (level == 3){
            dialogBinding.nextLevel.visibility = View.GONE
        }

        dialogBinding.nextLevel.setOnClickListener {
            findNavController().popBackStack(R.id.gameFragment, true)
            findNavController().navigate(R.id.gameFragment, bundleOf(
                "selectedLevel" to level+1
            ))
            customDialog.dismiss()
        }

        dialogBinding.replay.setOnClickListener {
            replay = true
            findNavController().popBackStack(R.id.gameFragment, true)
            findNavController().navigate(R.id.gameFragment, bundleOf(
                "selectedLevel" to level
            ))
            customDialog.dismiss()
        }

        dialogBinding.menu.setOnClickListener {
            findNavController().navigate(R.id.action_gameFragment_to_mainMenuFragment)
            customDialog.dismiss()
        }

        customDialog.show()
    }

    private fun vibrate(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            vibrator.vibrate(300)
        }
    }

}