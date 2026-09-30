package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Commute
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VideoCameraFront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MarketplaceItem
import com.example.ui.components.ProductCard
import com.example.ui.theme.PrimaNavy
import com.example.ui.theme.PrimaNavyContainer
import com.example.ui.theme.PrimaOnSurface
import com.example.ui.theme.PrimaOnSurfaceVariant
import com.example.ui.theme.PrimaSecondary
import com.example.ui.theme.PrimaSurfaceContainer
import com.example.ui.theme.PrimaSurfaceContainerHigh
import com.example.ui.theme.PrimaSurfaceContainerLow
import com.example.ui.theme.PrimaTertiaryContainer
import com.example.ui.theme.PrimaTertiaryFixed
import com.example.ui.theme.PrimaTertiaryFixedDim
import com.example.ui.theme.PrimaTextNavy
import com.example.ui.theme.PrimaTextSecondary
import com.example.ui.viewmodel.MarketplaceViewModel

@Composable
fun MarketplaceHomeScreen(
    viewModel: MarketplaceViewModel,
    onNavigateToDetail: (Long) -> Unit,
    onOpenCreateListing: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items by viewModel.filteredItems.collectAsState()
    val activeLoc by viewModel.locationFilter.collectAsState()
    val activeCat by viewModel.categoryFilter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // 1. Bazaar Announcement & Crew Alert Callout Banner
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(PrimaNavy)
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Bazar Warga MP",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Disahkan",
                                        tint = PrimaTertiaryFixedDim,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(PrimaNavyContainer)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "100% Kakitangan Sah",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Platform jual-beli rasmi krew & warga Media Prima Berhad.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFFF1F5F9),
                                    fontWeight = FontWeight.Medium
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Field Crew Alert Callout
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White.copy(alpha = 0.16f))
                                    .padding(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Commute,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Krew Luar Pejabat & Liputan?",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    )
                                    Text(
                                        text = "Tetapkan titik serahan lobi utama atau meja kerja rakan sebelum jam 5:00 petang.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFFF8FAFC),
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 11.sp,
                                            lineHeight = 15.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Location Switcher Pills
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LocationPill(
                        label = "Semua Lokasi",
                        icon = Icons.Default.Domain,
                        isSelected = activeLoc == "all",
                        onClick = { viewModel.setLocationFilter("all") }
                    )
                    LocationPill(
                        label = "Sri Pentas (BU)",
                        icon = Icons.Default.Tv,
                        isSelected = activeLoc == "sri-pentas",
                        onClick = { viewModel.setLocationFilter("sri-pentas") }
                    )
                    LocationPill(
                        label = "Balai Berita (Bangsar)",
                        icon = Icons.Default.Newspaper,
                        isSelected = activeLoc == "bangsar",
                        onClick = { viewModel.setLocationFilter("bangsar") }
                    )
                    LocationPill(
                        label = "Studio Glenmarie",
                        icon = Icons.Default.VideoCameraFront,
                        isSelected = activeLoc == "glenmarie",
                        onClick = { viewModel.setLocationFilter("glenmarie") }
                    )
                }
            }

            // 3. Search Bar & Filter Button
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("marketplace_search_input"),
                        placeholder = {
                            Text(
                                "Cari sarapan, gajet, jaket krew...",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 13.sp,
                                    color = PrimaTextSecondary
                                )
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Cari",
                                tint = PrimaTextNavy,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Padam",
                                        tint = PrimaOnSurface,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = PrimaSecondary,
                            unfocusedBorderColor = Color(0xFFCBD5E1),
                            focusedTextColor = PrimaOnSurface,
                            unfocusedTextColor = PrimaOnSurface
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaSurfaceContainerHigh)
                            .clickable {
                                val next = if (activeCat == "all") "makanan" else "all"
                                viewModel.setCategoryFilter(next)
                            }
                            .testTag("btn_toggle_filter"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Tapis Kategori",
                            tint = PrimaTextNavy,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // 4. Horizontal Categories
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CategoryPill(
                        label = "🔥 Hangat",
                        isSelected = activeCat == "all",
                        onClick = { viewModel.setCategoryFilter("all") }
                    )
                    CategoryPill(
                        label = "🍱 Makanan Pagi",
                        isSelected = activeCat == "makanan",
                        onClick = { viewModel.setCategoryFilter("makanan") }
                    )
                    CategoryPill(
                        label = "💻 Gajet & IT",
                        isSelected = activeCat == "gajet",
                        onClick = { viewModel.setCategoryFilter("gajet") }
                    )
                    CategoryPill(
                        label = "🧥 Pakaian Krew",
                        isSelected = activeCat == "pakaian",
                        onClick = { viewModel.setCategoryFilter("pakaian") }
                    )
                    CategoryPill(
                        label = "📦 Lain-lain",
                        isSelected = activeCat == "lain",
                        onClick = { viewModel.setCategoryFilter("lain") }
                    )
                }
            }

            // 5. Live Feed Summary Counter
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(PrimaSecondary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Iklan Terkini Warga",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaTextNavy
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaSurfaceContainer)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${items.size} Barangan Aktif",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = PrimaOnSurface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            // 6. Vertical Product Cards Feed
            if (items.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(PrimaSurfaceContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inventory2,
                                contentDescription = null,
                                tint = PrimaTextNavy,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Tiada Barangan Dijumpai",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaTextNavy
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Cuba tukar kata kunci atau pilih lokasi lain untuk melihat barang jualan rakan sekerja.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = PrimaOnSurfaceVariant,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            ),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                viewModel.setLocationFilter("all")
                                viewModel.setCategoryFilter("all")
                                viewModel.setSearchQuery("")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaNavy,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Tunjuk Semua Barang", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                items(items, key = { it.id }) { product ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        ProductCard(
                            item = product,
                            onDetailClick = { onNavigateToDetail(product.id) },
                            onWhatsAppClick = {
                                val text = "Salam ${product.sellerShortName}, saya kakitangan Media Prima. Saya tengok iklan PrimaBazaar untuk \"${product.title}\" (${product.priceFormatted}). Masih ada lagi? Boleh saya jumpa di titik serahan: ${product.pickupDetail}?"
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse("https://wa.me/${product.sellerPhone.replace("+", "").replace(" ", "").replace("-", "")}?text=${Uri.encode(text)}")
                                }
                                try {
                                    context.startActivity(intent)
                                } catch (_: Exception) {
                                    viewModel.showToast("Membuka sambungan WhatsApp ke ${product.sellerName}")
                                }
                            }
                        )
                    }
                }
            }

            // 7. Office Handover Notice Box
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(PrimaSurfaceContainer)
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Handshake,
                                    contentDescription = null,
                                    tint = PrimaSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Etika Serahan Warga Media Prima",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaTextNavy
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Pilih bayaran imbasan DuitNow QR atau tunai sewaktu pertemuan di lobi bersekuriti bagi memastikan keselamatan transaksi bersama rakan sekerja.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = PrimaOnSurfaceVariant,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Floating "+ Jual Barang" Action Pill
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 80.dp)
        ) {
            FloatingActionButton(
                onClick = onOpenCreateListing,
                shape = RoundedCornerShape(24.dp),
                containerColor = PrimaTertiaryContainer,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .height(48.dp)
                    .testTag("floating_btn_jual_barang")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = null,
                        tint = PrimaTertiaryFixedDim,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "+ Jual Barang",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationPill(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) PrimaNavy else PrimaSurfaceContainerHigh)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else PrimaOnSurface,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else PrimaOnSurface,
                    fontSize = 12.sp
                )
            )
        }
    }
}

@Composable
private fun CategoryPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) PrimaNavy else PrimaSurfaceContainerHigh)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else PrimaOnSurface,
                fontSize = 12.sp
            )
        )
    }
}
