package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ChatMessage
import com.example.data.model.MarketplaceItem
import com.example.data.model.OrderItem
import com.example.data.model.ReviewItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PrimaBazaarDao {

    @Query("SELECT * FROM marketplace_items ORDER BY id DESC")
    fun getAllItems(): Flow<List<MarketplaceItem>>

    @Query("SELECT * FROM marketplace_items WHERE id = :id LIMIT 1")
    fun getItemById(id: Long): Flow<MarketplaceItem?>

    @Query("SELECT COUNT(*) FROM marketplace_items")
    suspend fun getItemsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: MarketplaceItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllItems(items: List<MarketplaceItem>)

    @Update
    suspend fun updateItem(item: MarketplaceItem)

    @Delete
    suspend fun deleteItem(item: MarketplaceItem)

    @Query("UPDATE marketplace_items SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmark(id: Long, isBookmarked: Boolean)

    @Query("SELECT * FROM orders ORDER BY id ASC")
    fun getAllOrders(): Flow<List<OrderItem>>

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    fun getOrderById(id: Long): Flow<OrderItem?>

    @Query("SELECT COUNT(*) FROM orders")
    suspend fun getOrdersCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllOrders(orders: List<OrderItem>)

    @Update
    suspend fun updateOrder(order: OrderItem)

    @Query("UPDATE orders SET isRead = 1, unreadCount = 0 WHERE id = :orderId")
    suspend fun markOrderAsRead(orderId: Long)

    @Query("UPDATE orders SET isRead = 1, unreadCount = 0")
    suspend fun markAllOrdersAsRead()

    @Query("UPDATE orders SET isDuitNowCompleted = 1, orderStatus = 'SELESAI', duitNowRef = :ref WHERE id = :orderId")
    suspend fun completeDuitNowPayment(orderId: Long, ref: String)

    @Query("SELECT * FROM chat_messages WHERE orderId = :orderId ORDER BY id ASC")
    fun getChatMessages(orderId: Long): Flow<List<ChatMessage>>

    @Query("SELECT COUNT(*) FROM chat_messages")
    suspend fun getChatCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllChatMessages(messages: List<ChatMessage>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewItem): Long

    @Query("SELECT * FROM reviews WHERE itemId = :itemId ORDER BY id DESC")
    fun getReviewsForItem(itemId: Long): Flow<List<ReviewItem>>
}
