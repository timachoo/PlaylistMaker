package com.example.playlistmaker

data class Track(
    val trackId: String, // Идентификатор трека
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Int, // Продолжительность трека в миллисекундах
    val trackTime: String, // Продолжительность трека
    val artworkUrl100: String // Ссылка на изображение обложки
)
