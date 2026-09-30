package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderId: Long,
    val sender: String, // "buyer" or "seller"
    val senderName: String,
    val text: String,
    val timestamp: String,
    val isDuitNowCard: Boolean = false,
    val isReceiptCard: Boolean = false,
    val duitNowAmount: String? = null,
    val duitNowRef: String? = null
)
