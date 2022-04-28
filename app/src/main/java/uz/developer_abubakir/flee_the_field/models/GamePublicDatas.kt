package uz.developer_abubakir.flee_the_field.models

import android.content.Context
import android.media.MediaPlayer
import com.orhanobut.hawk.Hawk
import uz.developer_abubakir.flee_the_field.R

object GamePublicDatas {
    var settings: Settings? = null
    var mainAudioPlayer: MediaPlayer? = null

    fun write(){
        Hawk.put("settings", settings)
    }

    fun playMusicByRaw(context: Context, raw: Int){
        if (settings!!.sound) {
            if (mainAudioPlayer != null) {
                mainAudioPlayer!!.stop()
            }

            mainAudioPlayer = MediaPlayer.create(context, raw)
            if (raw == R.raw.menu || raw == R.raw.game){
                mainAudioPlayer!!.isLooping = true
            }
            mainAudioPlayer!!.start()
        }
    }

    fun stopMusic(){
        if (mainAudioPlayer != null) {
            mainAudioPlayer!!.stop()
            mainAudioPlayer = null
        }
    }

    fun playMusic(){
        if (mainAudioPlayer != null) {
            mainAudioPlayer!!.start()
        }
    }

    fun pauseMusic(){
        if (mainAudioPlayer != null) {
            mainAudioPlayer!!.pause()
        }
    }

    fun isMusicPlayed():Boolean{
        return if (mainAudioPlayer != null){
            mainAudioPlayer!!.isPlaying
        }else{
            false
        }
    }
}