package com.cbuildz.tvpro.ui

import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.ButtonDefaults as TvButtonDefaults
import androidx.tv.material3.ButtonColors
import androidx.compose.material3.MaterialTheme

object TVButtonDefaults {
    @Composable
    fun colors(): ButtonColors {
        return TvButtonDefaults.colors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
            focusedContentColor = Color.White
        )
    }
}
