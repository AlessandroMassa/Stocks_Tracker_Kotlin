package com.massa844853.stockstracker.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class News(var title: String, var link: String, var publisher: String, var published_at: Long, var main_image: String) {
    @PrimaryKey(autoGenerate = true)
    var id = 0

}