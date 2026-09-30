package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "marketplace_items")
data class MarketplaceItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val price: Double,
    val priceFormatted: String,
    val category: String, // "makanan", "gajet", "pakaian", "lain"
    val categoryLabel: String,
    val locationHub: String, // "sri-pentas", "bangsar", "glenmarie"
    val locationLabel: String,
    val pickupDetail: String,
    val badge: String,
    val condition: String,
    val sellerName: String,
    val sellerShortName: String,
    val sellerDept: String,
    val sellerSso: String,
    val sellerPhone: String,
    val sellerInitials: String,
    val description: String,
    val variations: String,
    val handoverTime: String,
    val handoverNotes: String,
    val paymentMethods: String,
    val imageUrl: String,
    val secondaryImageUrl: String,
    val thirdImageUrl: String,
    val isBookmarked: Boolean = false,
    val viewsCount: Int = 84,
    val publishedTimeAgo: String = "35 minit lepas",
    val isNegotiable: Boolean = true,
    val completedSales: Int = 18
)
