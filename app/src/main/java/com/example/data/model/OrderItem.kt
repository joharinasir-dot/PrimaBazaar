package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val itemId: Long,
    val itemTitle: String,
    val itemPrice: Double,
    val itemPriceFormatted: String,
    val sellerName: String,
    val sellerDept: String,
    val sellerPhone: String,
    val sellerExt: String,
    val imageUrl: String,
    val pickupSpot: String,
    val pickupTime: String,
    val orderStatus: String, // "PERINGATAN", "BERJAYA", "DALAM_PROSES", "SELESAI", "TAWARAN"
    val orderType: String, // "BELIAN", "JUALAN"
    val lastMessage: String,
    val unreadCount: Int = 0,
    val timestamp: String,
    val isRead: Boolean = true,
    val offerPrice: Double? = null,
    val isDuitNowCompleted: Boolean = false,
    val duitNowRef: String? = null
)
