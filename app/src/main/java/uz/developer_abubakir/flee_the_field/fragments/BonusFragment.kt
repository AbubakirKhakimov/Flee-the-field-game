package uz.developer_abubakir.flee_the_field.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.developer_abubakir.flee_the_field.R
import uz.developer_abubakir.flee_the_field.databinding.FragmentBonusBinding

class BonusFragment : Fragment() {

    lateinit var binding: FragmentBonusBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBonusBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.levels.setOnClickListener {
            findNavController().navigate(R.id.action_bonusFragment_to_levelsFragment)
        }

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}