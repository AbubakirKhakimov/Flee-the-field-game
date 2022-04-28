package uz.developer_abubakir.flee_the_field.models

data class Settings(
    var bonusCount: Int,
    var getLastBonusTime: Long,
    var sound: Boolean,
    var vibration: Boolean,
    var level: Int
)
