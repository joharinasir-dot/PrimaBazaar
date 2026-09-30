package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.PrimaBazaarDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.MarketplaceItem
import com.example.data.model.OrderItem
import com.example.data.model.ReviewItem
import com.example.data.repository.MarketplaceRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class PrimaTab {
    PASAR,
    CARIAN,
    CREATE,
    PESANAN,
    PROFIL
}

class MarketplaceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MarketplaceRepository

    init {
        val db = PrimaBazaarDatabase.getDatabase(application)
        repository = MarketplaceRepository(db.dao())
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    private val _currentTab = MutableStateFlow(PrimaTab.PASAR)
    val currentTab: StateFlow<PrimaTab> = _currentTab.asStateFlow()

    private val _selectedItemId = MutableStateFlow<Long?>(null)
    val selectedItemId: StateFlow<Long?> = _selectedItemId.asStateFlow()

    private val _selectedOrderId = MutableStateFlow<Long?>(null)
    val selectedOrderId: StateFlow<Long?> = _selectedOrderId.asStateFlow()

    private val _isCreatingListing = MutableStateFlow(false)
    val isCreatingListing: StateFlow<Boolean> = _isCreatingListing.asStateFlow()

    // Filters
    private val _locationFilter = MutableStateFlow("all")
    val locationFilter: StateFlow<String> = _locationFilter.asStateFlow()

    private val _categoryFilter = MutableStateFlow("all")
    val categoryFilter: StateFlow<String> = _categoryFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _orderTabFilter = MutableStateFlow("ALL") // "ALL", "BELIAN", "JUALAN"
    val orderTabFilter: StateFlow<String> = _orderTabFilter.asStateFlow()

    private val _orderStatusFilter = MutableStateFlow("ALL") // "ALL", "UNREAD", "HANDOVER", "COMPLETED"
    val orderStatusFilter: StateFlow<String> = _orderStatusFilter.asStateFlow()

    // Modals
    private val _showDuitNowModal = MutableStateFlow(false)
    val showDuitNowModal: StateFlow<Boolean> = _showDuitNowModal.asStateFlow()

    private val _showRatingModal = MutableStateFlow(false)
    val showRatingModal: StateFlow<Boolean> = _showRatingModal.asStateFlow()

    private val _showFilterSheet = MutableStateFlow(false)
    val showFilterSheet: StateFlow<Boolean> = _showFilterSheet.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Filtered Items Flow
    val allItems: StateFlow<List<MarketplaceItem>> = repository.allItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val filteredItems: StateFlow<List<MarketplaceItem>> = combine(
        allItems,
        _locationFilter,
        _categoryFilter,
        _searchQuery
    ) { items, loc, cat, query ->
        items.filter { item ->
            val matchLoc = (loc == "all") || (item.locationHub == loc)
            val matchCat = (cat == "all") || (item.category == cat)
            val matchQuery = query.isBlank() ||
                    item.title.contains(query, ignoreCase = true) ||
                    item.sellerName.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true) ||
                    item.locationLabel.contains(query, ignoreCase = true)
            matchLoc && matchCat && matchQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Filtered Orders Flow
    val allOrders: StateFlow<List<OrderItem>> = repository.allOrders.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val filteredOrders: StateFlow<List<OrderItem>> = combine(
        allOrders,
        _orderTabFilter,
        _orderStatusFilter,
        _searchQuery
    ) { orders, typeTab, statusFilter, query ->
        orders.filter { order ->
            val matchType = when (typeTab) {
                "BELIAN" -> order.orderType == "BELIAN"
                "JUALAN" -> order.orderType == "JUALAN"
                else -> true
            }
            val matchStatus = when (statusFilter) {
                "UNREAD" -> !order.isRead || order.unreadCount > 0
                "HANDOVER" -> order.orderStatus == "PERINGATAN" || order.orderStatus == "DALAM_PROSES"
                "COMPLETED" -> order.orderStatus == "SELESAI"
                else -> true
            }
            val matchQuery = query.isBlank() ||
                    order.itemTitle.contains(query, ignoreCase = true) ||
                    order.sellerName.contains(query, ignoreCase = true)
            matchType && matchStatus && matchQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Chat messages for selected order
    private val _activeChatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val activeChatMessages: StateFlow<List<ChatMessage>> = _activeChatMessages.asStateFlow()

    fun selectTab(tab: PrimaTab) {
        if (tab == PrimaTab.CREATE) {
            _isCreatingListing.value = true
        } else {
            _currentTab.value = tab
            _isCreatingListing.value = false
            _selectedItemId.value = null
            _selectedOrderId.value = null
        }
    }

    fun selectItem(id: Long) {
        _selectedItemId.value = id
    }

    fun clearSelectedItem() {
        _selectedItemId.value = null
    }

    fun selectOrder(id: Long) {
        _selectedOrderId.value = id
        viewModelScope.launch {
            repository.markOrderAsRead(id)
            repository.getChatMessages(id).collect { messages ->
                _activeChatMessages.value = messages
            }
        }
    }

    fun clearSelectedOrder() {
        _selectedOrderId.value = null
    }

    fun openCreateListing() {
        _isCreatingListing.value = true
    }

    fun closeCreateListing() {
        _isCreatingListing.value = false
    }

    fun setLocationFilter(loc: String) {
        _locationFilter.value = loc
    }

    fun setCategoryFilter(cat: String) {
        _categoryFilter.value = cat
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setOrderTabFilter(filter: String) {
        _orderTabFilter.value = filter
    }

    fun setOrderStatusFilter(filter: String) {
        _orderStatusFilter.value = filter
    }

    fun markAllOrdersAsRead() {
        viewModelScope.launch {
            repository.markAllOrdersAsRead()
            showToast("Semua pesanan ditandai sebagai dibaca")
        }
    }

    fun setShowDuitNowModal(show: Boolean) {
        _showDuitNowModal.value = show
    }

    fun setShowRatingModal(show: Boolean) {
        _showRatingModal.value = show
    }

    fun setShowFilterSheet(show: Boolean) {
        _showFilterSheet.value = show
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
        viewModelScope.launch {
            delay(2800)
            if (_toastMessage.value == msg) {
                _toastMessage.value = null
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun toggleBookmark(id: Long, currentVal: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(id, !currentVal)
            val msg = if (!currentVal) "Iklan disimpan dalam senarai kegemaran" else "Iklan dikeluarkan dari kegemaran"
            showToast(msg)
        }
    }

    fun sendChatMessage(orderId: Long, text: String) {
        if (text.isBlank()) return
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val timeStr = timeFormat.format(Date())

        viewModelScope.launch {
            val userMsg = ChatMessage(
                orderId = orderId,
                sender = "buyer",
                senderName = "Nurul Aini",
                text = text,
                timestamp = timeStr
            )
            repository.sendChatMessage(userMsg)

            // Dynamic response simulation
            delay(1200)
            val replyText = when {
                text.contains("DuitNow", ignoreCase = true) || text.contains("transfer", ignoreCase = true) ->
                    "Alhamdulillah terima kasih Nurul! Duit dah masuk elok. Bungkusan akak dah siapkan atas meja kafeteria ya ❤️"
                text.contains("turun", ignoreCase = true) || text.contains("sekarang", ignoreCase = true) ->
                    "Baik Nurul, jumpa di kafeteria nanti! Terima kasih sentiasa sokong jualan krew!"
                text.contains("sekuriti", ignoreCase = true) ->
                    "Boleh sangat, nanti akak titip bungkusan kat kaunter sekuriti lobi utama bertanda nama Nurul ya!"
                else ->
                    "Terima kasih Nurul! Jumpa di kafeteria nanti ya. Selamat bertugas hari ini! ✨"
            }
            val replyMsg = ChatMessage(
                orderId = orderId,
                sender = "seller",
                senderName = "Kak Ani (TV3)",
                text = replyText,
                timestamp = timeFormat.format(Date())
            )
            repository.sendChatMessage(replyMsg)
        }
    }

    fun confirmDuitNowPayment(orderId: Long, ref: String) {
        viewModelScope.launch {
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val timeStr = timeFormat.format(Date())

            val receiptMsg = ChatMessage(
                orderId = orderId,
                sender = "buyer",
                senderName = "Nurul Aini",
                text = "Resit DuitNow RM 6.00 berjaya dipindahkan ke Maybank Kak Ani (Ref: $ref).",
                timestamp = timeStr,
                isReceiptCard = true,
                duitNowRef = ref,
                duitNowAmount = "RM 6.00"
            )
            repository.sendChatMessage(receiptMsg)
            repository.completeDuitNowPayment(orderId, ref)
            _showDuitNowModal.value = false
            showToast("Bayaran DuitNow $ref berjaya dimaklumkan!")

            // Auto-acknowledge by seller
            delay(1200)
            val ackMsg = ChatMessage(
                orderId = orderId,
                sender = "seller",
                senderName = "Kak Ani (TV3)",
                text = "Alhamdulillah terima kasih Nurul! Duit dah masuk. Akak dah simpan tepi elok-elok bungkusan bersambal lebih atas kaunter minuman berserta label nama Nurul. Selamat menjamu selera! 👍❤️",
                timestamp = timeFormat.format(Date())
            )
            repository.sendChatMessage(ackMsg)
        }
    }

    fun submitRating(
        itemId: Long,
        sellerName: String,
        rating: Int,
        compliments: List<String>,
        reviewText: String,
        showIdentity: Boolean
    ) {
        viewModelScope.launch {
            val timeFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val review = ReviewItem(
                itemId = itemId,
                sellerName = sellerName,
                rating = rating,
                compliments = compliments.joinToString(", "),
                reviewText = reviewText,
                reviewerName = if (showIdentity) "Nurul Aini" else "Kakitangan TV3 (Tanpa Nama)",
                reviewerDept = "Berita & Hal Ehwal Semasa",
                showIdentity = showIdentity,
                timestamp = timeFormat.format(Date())
            )
            repository.submitReview(review)
            _showRatingModal.value = false
            showToast("Ulasan $rating Bintang diterima! Terima kasih atas sokongan warga MP 🎉")
        }
    }

    fun acceptOffer(orderId: Long) {
        viewModelScope.launch {
            val order = repository.getOrderById(orderId)
            showToast("Tawaran diterima! Makluman dihantar kepada pembeli.")
        }
    }

    fun declineOffer(orderId: Long) {
        viewModelScope.launch {
            showToast("Tawaran telah ditolak dengan mesra.")
        }
    }

    fun publishNewListing(
        title: String,
        priceText: String,
        category: String,
        categoryLabel: String,
        condition: String,
        description: String,
        hub: String,
        hubLabel: String,
        pickupSpot: String,
        timeFrom: String,
        timeTo: String,
        waPhone: String,
        isNego: Boolean
    ) {
        val priceDouble = priceText.toDoubleOrNull() ?: 10.0
        val newItem = MarketplaceItem(
            title = title.ifBlank { "Barangan Kakitangan TV3" },
            price = priceDouble,
            priceFormatted = "RM ${String.format(Locale.US, "%.2f", priceDouble)}",
            category = category,
            categoryLabel = categoryLabel,
            locationHub = hub,
            locationLabel = hubLabel,
            pickupDetail = "$hubLabel • $pickupSpot",
            badge = "Iklan Terkini Warga",
            condition = condition,
            sellerName = "Nurul Aini Binti Yusof",
            sellerShortName = "Nurul Aini",
            sellerDept = "Jabatan Berita & HE Semasa • Sri Pentas",
            sellerSso = "MP-88421",
            sellerPhone = waPhone.ifBlank { "+60193827109" },
            sellerInitials = "NA",
            description = description.ifBlank { "Barangan elok milik warga Media Prima Berhad." },
            variations = "Sedia Serah Terima Pejabat",
            handoverTime = "$timeFrom – $timeTo",
            handoverNotes = "Penyerahan selamat boleh ditinggalkan di kaunter bantuan (concierge) lobi sekiranya anda bertugas di luar stesen.",
            paymentMethods = "DuitNow QR, Tunai Semasa Serahan (COD)",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDAq1QWJJhCJ-Dzgip-VUFL3FJGMg6hx1BYGqNCDD2EDABJwvo1MVChTSxjLrucKU-0Zp8wl98xgtruVE6OE9CFV5V0fpnev4dkISrumAdDLb7T9aQAwqWNaSkobySzBnMb3mg4fUBGDrrOguzom9I5AmEXUUz7dFfMjSrrICHmFauGKD7Sn0An6JQsNSl7dKQjuBErRUs3VbNkxFYX3-xblxBhvqBzf8qVQlE7Pu582rGBVzjKL-0o0w",
            secondaryImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCgTRs4Q2D9plJY6cat_31BT0EAc8qf2_aH6IZuCWaEciLCOf0OeQ5_cFLsNHptqEzFxR-RUH5F484l_ESkRS8v9wO6g2YzUpjKeI6CTUu_N-p0BsDDKewjmTKEI7-lcsYbJ0zcesSp7ViBrAxye1sc8riNhj_5budbcH_ViXGYkpFt9IGoSRq0ev1_NEFSI43rVbWpxB69Tfwj97IHFhYzUEqMrisEhz10cHtDJxYDiQz-Hph9wqASOQ",
            thirdImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDAq1QWJJhCJ-Dzgip-VUFL3FJGMg6hx1BYGqNCDD2EDABJwvo1MVChTSxjLrucKU-0Zp8wl98xgtruVE6OE9CFV5V0fpnev4dkISrumAdDLb7T9aQAwqWNaSkobySzBnMb3mg4fUBGDrrOguzom9I5AmEXUUz7dFfMjSrrICHmFauGKD7Sn0An6JQsNSl7dKQjuBErRUs3VbNkxFYX3-xblxBhvqBzf8qVQlE7Pu582rGBVzjKL-0o0w",
            isBookmarked = false,
            viewsCount = 1,
            publishedTimeAgo = "Baru sebentar tadi",
            isNegotiable = isNego,
            completedSales = 3
        )

        viewModelScope.launch {
            repository.insertItem(newItem)
            _isCreatingListing.value = false
            _currentTab.value = PrimaTab.PASAR
            showToast("Iklan anda berjaya diterbitkan serta-merta ke Bazar Warga MP! 🚀")
        }
    }
}
