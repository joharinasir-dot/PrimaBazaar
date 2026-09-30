package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val itemId: Long,
    val sellerName: String,
    val rating: Int,
    val compliments: String,
    val reviewText: String,
    val reviewerName: String,
    val reviewerDept: String,
    val showIdentity: Boolean = true,
    val timestamp: String
)
