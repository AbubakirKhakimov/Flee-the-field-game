package uz.developer_abubakir.flee_the_field.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import uz.developer_abubakir.flee_the_field.R
import uz.developer_abubakir.flee_the_field.adapters.LevelsAdapter
import uz.developer_abubakir.flee_the_field.adapters.LevelsAdapterCallBack
import uz.developer_abubakir.flee_the_field.databinding.FragmentLevelsBinding
import uz.developer_abubakir.flee_the_field.models.GamePublicDatas

class LevelsFragment : Fragment(), LevelsAdapterCallBack {

    lateinit var binding: FragmentLevelsBinding
    lateinit var levelsAdapter: LevelsAdapter
    val settings = GamePublicDatas.settings!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLevelsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!GamePublicDatas.isMusicPlayed()){
            GamePublicDatas.playMusicByRaw(requireActivity(), R.raw.menu)
        }

        levelsAdapter = LevelsAdapter(3, settings.level, requireActivity(), this)
        binding.levelsRv.adapter = levelsAdapter

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun itemSelectedListener(level: Int) {
        if (isActiveLevel(level)) {
            findNavController().navigate(
                R.id.action_levelsFragment_to_gameFragment, bundleOf(
                    "selectedLevel" to level
                )
            )
        }
    }

    private fun isActiveLevel(level: Int):Boolean{
        return level <= GamePublicDatas.settings!!.level
    }

}