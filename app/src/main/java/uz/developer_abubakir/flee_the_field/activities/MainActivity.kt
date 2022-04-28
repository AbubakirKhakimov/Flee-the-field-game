package uz.developer_abubakir.flee_the_field.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MotionEventCompat
import com.orhanobut.hawk.Hawk
import uz.developer_abubakir.flee_the_field.R
import uz.developer_abubakir.flee_the_field.models.GamePublicDatas
import uz.developer_abubakir.flee_the_field.models.Settings
import uz.developer_abubakir.flee_the_field.utils.LocaleManager


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        read()
        setContentView(R.layout.activity_main)
    }

    override fun attachBaseContext(newBase: Context?) {
        Hawk.init(newBase).build()
        super.attachBaseContext(LocaleManager.setLocale(newBase))
    }

    private fun read(){
        GamePublicDatas.settings = Hawk.get("settings", null)
        if (GamePublicDatas.settings == null){
            GamePublicDatas.settings = Settings(3, 0L, true, true, 1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GamePublicDatas.stopMusic()
    }

    override fun onStop() {
        super.onStop()
        GamePublicDatas.pauseMusic()
    }

    override fun onStart() {
        super.onStart()
        GamePublicDatas.playMusic()
    }

}