package com.arribas.myshoppinglist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Shoplist(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val type: String
)
