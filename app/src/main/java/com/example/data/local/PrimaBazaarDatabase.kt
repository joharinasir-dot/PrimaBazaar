package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.MarketplaceItem
import com.example.data.model.OrderItem
import com.example.data.model.ReviewItem

@Database(
    entities = [
        MarketplaceItem::class,
        OrderItem::class,
        ChatMessage::class,
        ReviewItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PrimaBazaarDatabase : RoomDatabase() {
    abstract fun dao(): PrimaBazaarDao

    companion object {
        @Volatile
        private var INSTANCE: PrimaBazaarDatabase? = null

        fun getDatabase(context: Context): PrimaBazaarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PrimaBazaarDatabase::class.java,
                    "primabazaar_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
