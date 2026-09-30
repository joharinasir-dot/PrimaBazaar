package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrimaNavy
import com.example.ui.theme.PrimaNavyContainer
import com.example.ui.theme.PrimaSecondary
import com.example.ui.viewmodel.PrimaTab

@Composable
fun PrimaBottomNavBar(
    currentTab: PrimaTab,
    unreadOrderCount: Int = 2,
    onTabSelected: (PrimaTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(64.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tab 1: Pasar
                NavTabItem(
                    title = "Pasar",
                    selectedIcon = Icons.Filled.Storefront,
                    unselectedIcon = Icons.Outlined.Storefront,
                    isSelected = currentTab == PrimaTab.PASAR,
                    onClick = { onTabSelected(PrimaTab.PASAR) },
                    testTag = "nav_tab_pasar"
                )

                // Tab 2: Carian
                NavTabItem(
                    title = "Carian",
                    selectedIcon = Icons.Filled.Explore,
                    unselectedIcon = Icons.Outlined.Explore,
                    isSelected = currentTab == PrimaTab.CARIAN,
                    onClick = { onTabSelected(PrimaTab.CARIAN) },
                    testTag = "nav_tab_carian"
                )

                // Center placeholder for floating + Iklan
                Spacer(modifier = Modifier.size(54.dp))

                // Tab 4: Pesanan
                NavTabItem(
                    title = "Pesanan",
                    selectedIcon = Icons.Filled.Chat,
                    unselectedIcon = Icons.Outlined.Chat,
                    isSelected = currentTab == PrimaTab.PESANAN,
                    badgeCount = unreadOrderCount,
                    onClick = { onTabSelected(PrimaTab.PESANAN) },
                    testTag = "nav_tab_pesanan"
                )

                // Tab 5: Profil
                NavTabItem(
                    title = "Profil",
                    selectedIcon = Icons.Filled.Badge,
                    unselectedIcon = Icons.Outlined.Badge,
                    isSelected = currentTab == PrimaTab.PROFIL,
                    onClick = { onTabSelected(PrimaTab.PROFIL) },
                    testTag = "nav_tab_profil"
                )
            }

            // Floating Center + Iklan Button
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-14).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FloatingActionButton(
                    onClick = { onTabSelected(PrimaTab.CREATE) },
                    shape = CircleShape,
                    containerColor = PrimaNavyContainer,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .size(50.dp)
                        .testTag("nav_btn_add_listing")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah Iklan Baru",
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "+ Iklan",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = PrimaNavy,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun NavTabItem(
    title: String,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    isSelected: Boolean,
    badgeCount: Int = 0,
    onClick: () -> Unit,
    testTag: String
) {
    val interactionSource = remember { MutableInteractionSource() }
    val tint = if (isSelected) PrimaNavy else Color(0xFF334155)
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Icon(
                imageVector = if (isSelected) selectedIcon else unselectedIcon,
                contentDescription = title,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
            if (badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .offset(x = 6.dp, y = (-3).dp)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFBA1A1A)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badgeCount.toString(),
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                color = tint,
                fontWeight = fontWeight,
                fontSize = 11.sp
            ),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
