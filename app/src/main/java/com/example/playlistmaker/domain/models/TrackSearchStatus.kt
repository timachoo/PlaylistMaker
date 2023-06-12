package com.example.playlistmaker.domain.models

enum class TrackSearchStatus {
    Success  // Успех
    , NoDataFound // Не нашли
    , ConnectionError // Ошибка
    , ShowHistory // История
    , Empty // Пусто
    , Progress // Ожидание
}