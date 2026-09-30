package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.PrimaBottomNavBar
import com.example.ui.components.PrimaTopBar
import com.example.ui.screens.ChatDetailScreen
import com.example.ui.screens.CreateListingScreen
import com.example.ui.screens.ExploreSearchScreen
import com.example.ui.screens.ItemDetailScreen
import com.example.ui.screens.MarketplaceHomeScreen
import com.example.ui.screens.OrdersAndChatScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PrimaInverseSurface
import com.example.ui.viewmodel.MarketplaceViewModel
import com.example.ui.viewmodel.PrimaTab

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: MarketplaceViewModel = viewModel()
                PrimaBazaarApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun PrimaBazaarApp(viewModel: MarketplaceViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val selectedItemId by viewModel.selectedItemId.collectAsState()
    val selectedOrderId by viewModel.selectedOrderId.collectAsState()
    val isCreatingListing by viewModel.isCreatingListing.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    // Global back handling
    BackHandler(enabled = isCreatingListing || selectedItemId != null || selectedOrderId != null || currentTab != PrimaTab.PASAR) {
        when {
            isCreatingListing -> viewModel.closeCreateListing()
            selectedItemId != null -> viewModel.clearSelectedItem()
            selectedOrderId != null -> viewModel.clearSelectedOrder()
            currentTab != PrimaTab.PASAR -> viewModel.selectTab(PrimaTab.PASAR)
        }
    }

    val showTopBar = !isCreatingListing && selectedItemId == null && selectedOrderId == null
    val showBottomBar = !isCreatingListing && selectedItemId == null && selectedOrderId == null

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showTopBar) {
                PrimaTopBar(
                    onNotificationsClick = {
                        viewModel.showToast("Tiada notifikasi baharu untuk waktu ini")
                    },
                    onProfileClick = {
                        viewModel.selectTab(PrimaTab.PROFIL)
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                PrimaBottomNavBar(
                    currentTab = currentTab,
                    unreadOrderCount = 2,
                    onTabSelected = { tab ->
                        viewModel.selectTab(tab)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                // Screen 3: Terbitkan Iklan
                isCreatingListing -> {
                    CreateListingScreen(
                        viewModel = viewModel,
                        onClose = { viewModel.closeCreateListing() }
                    )
                }

                // Screen 2: Butiran Barangan
                selectedItemId != null -> {
                    ItemDetailScreen(
                        itemId = selectedItemId!!,
                        viewModel = viewModel,
                        onBack = { viewModel.clearSelectedItem() }
                    )
                }

                // Sub-Screen: Chat Detail inside Pesanan
                selectedOrderId != null -> {
                    ChatDetailScreen(
                        orderId = selectedOrderId!!,
                        viewModel = viewModel,
                        onBack = { viewModel.clearSelectedOrder() }
                    )
                }

                // Screen 4: Pesanan & Sembang
                currentTab == PrimaTab.PESANAN -> {
                    OrdersAndChatScreen(
                        viewModel = viewModel,
                        onOpenChat = { orderId ->
                            viewModel.selectOrder(orderId)
                        }
                    )
                }

                // Secondary Tab: Carian & Kategori
                currentTab == PrimaTab.CARIAN -> {
                    ExploreSearchScreen(
                        viewModel = viewModel,
                        onNavigateToDetail = { itemId ->
                            viewModel.selectItem(itemId)
                        }
                    )
                }

                // Secondary Tab: Profil Kakitangan
                currentTab == PrimaTab.PROFIL -> {
                    ProfileScreen(
                        viewModel = viewModel,
                        onOpenCreateListing = { viewModel.openCreateListing() }
                    )
                }

                // Screen 1: PrimaBazaar Mobile - Pasar Kakitangan Media Prima (Initial Screen)
                else -> {
                    MarketplaceHomeScreen(
                        viewModel = viewModel,
                        onNavigateToDetail = { itemId ->
                            viewModel.selectItem(itemId)
                        },
                        onOpenCreateListing = {
                            viewModel.openCreateListing()
                        }
                    )
                }
            }

            // Global Toast Notification Overlay
            AnimatedVisibility(
                visible = toastMessage != null,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .padding(horizontal = 20.dp)
            ) {
                toastMessage?.let { msg ->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(PrimaInverseSurface)
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF34D399),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = msg,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
