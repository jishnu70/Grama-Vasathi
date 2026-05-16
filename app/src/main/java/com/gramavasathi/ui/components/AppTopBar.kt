package com.gramavasathi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gramavasathi.ui.theme.CreamWhite
import com.gramavasathi.ui.theme.DividerWarm
import com.gramavasathi.ui.theme.EarthBrown

@Composable
fun AppTopBar(title: String, onBack: (() -> Unit)? = null, actions: @Composable () -> Unit = {}) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(CreamWhite)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = "Back", tint = EarthBrown)
                }
            } else {
                Spacer(Modifier.padding(horizontal = 12.dp))
            }
            Text(
                title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = EarthBrown,
                modifier = Modifier.weight(1f)
            )
            actions()
        }
        // bottom divider
        Box(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(DividerWarm)
                .align(Alignment.BottomCenter)
        )
    }
}
