package com.example.data.repository

import com.example.data.local.PrimaBazaarDao
import com.example.data.model.ChatMessage
import com.example.data.model.InitialData
import com.example.data.model.MarketplaceItem
import com.example.data.model.OrderItem
import com.example.data.model.ReviewItem
import kotlinx.coroutines.flow.Flow

class MarketplaceRepository(private val dao: PrimaBazaarDao) {

    val allItems: Flow<List<MarketplaceItem>> = dao.getAllItems()
    val allOrders: Flow<List<OrderItem>> = dao.getAllOrders()

    suspend fun checkAndSeedInitialData() {
        if (dao.getItemsCount() == 0) {
            dao.insertAllItems(InitialData.items)
        }
        if (dao.getOrdersCount() == 0) {
            dao.insertAllOrders(InitialData.orders)
        }
        if (dao.getChatCount() == 0) {
            dao.insertAllChatMessages(InitialData.chatMessagesOrder1)
        }
    }

    fun getItemById(id: Long): Flow<MarketplaceItem?> = dao.getItemById(id)

    suspend fun insertItem(item: MarketplaceItem): Long = dao.insertItem(item)

    suspend fun toggleBookmark(id: Long, isBookmarked: Boolean) {
        dao.updateBookmark(id, isBookmarked)
    }

    fun getOrderById(id: Long): Flow<OrderItem?> = dao.getOrderById(id)

    suspend fun markOrderAsRead(orderId: Long) {
        dao.markOrderAsRead(orderId)
    }

    suspend fun markAllOrdersAsRead() {
        dao.markAllOrdersAsRead()
    }

    suspend fun updateOrder(order: OrderItem) {
        dao.updateOrder(order)
    }

    suspend fun completeDuitNowPayment(orderId: Long, ref: String) {
        dao.completeDuitNowPayment(orderId, ref)
    }

    fun getChatMessages(orderId: Long): Flow<List<ChatMessage>> = dao.getChatMessages(orderId)

    suspend fun sendChatMessage(message: ChatMessage): Long = dao.insertChatMessage(message)

    suspend fun submitReview(review: ReviewItem): Long = dao.insertReview(review)
}
