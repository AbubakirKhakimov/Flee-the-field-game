package uz.developer_abubakir.flee_the_field.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.orhanobut.hawk.Hawk
import uz.developer_abubakir.flee_the_field.R
import uz.developer_abubakir.flee_the_field.utils.CustomSwitchController
import uz.developer_abubakir.flee_the_field.databinding.FragmentSettingsBinding
import uz.developer_abubakir.flee_the_field.models.GamePublicDatas
import uz.developer_abubakir.flee_the_field.utils.CustomSwitchCallBack
import uz.developer_abubakir.flee_the_field.utils.LocaleManager

class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding
    lateinit var soundSwitch: CustomSwitchController
    lateinit var vibrationSwitch: CustomSwitchController

    val settings = GamePublicDatas.settings!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        soundSwitch = CustomSwitchController(
            requireActivity(),
            binding.soundCase,
            binding.soundSwitch,
            settings.sound,
            object : CustomSwitchCallBack{
                override fun switchStateChangedListener(state: Boolean) {
                    settings.sound = state
                    GamePublicDatas.playMusicByRaw(requireActivity(), R.raw.menu)
                    if (!state){
                        GamePublicDatas.stopMusic()
                    }
                }
            })

        vibrationSwitch = CustomSwitchController(
            requireActivity(),
            binding.vibrationCase,
            binding.vibrationSwitch,
            settings.vibration,
            object : CustomSwitchCallBack{
                override fun switchStateChangedListener(state: Boolean) {
                    settings.vibration = state
                }
            })

        binding.english.setOnClickListener {
            setLanguage("en")
        }

        binding.russian.setOnClickListener {
            setLanguage("ru")
        }

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun setLanguage(lang: String){
        Hawk.put("pref_lang", lang)
        LocaleManager.setLocale(requireActivity())
        findNavController().popBackStack(R.id.settingsFragment, true)
        findNavController().navigate(R.id.settingsFragment)
    }

    override fun onStop() {
        super.onStop()
        GamePublicDatas.write()
    }

}