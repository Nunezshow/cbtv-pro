package com.cbuildz.tvpro.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.tv.material3.ButtonColors
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api

@OptIn(ExperimentalTvMaterial3Api::class)
object TVButtonDefaults {
    @Composable
    fun colors(): ButtonColors {
        val cs = MaterialTheme.colorScheme
        return ButtonDefaults.colors(
            containerColor = cs.surfaceVariant,
            contentColor = cs.onSurface,
            focusedContainerColor = cs.primary,
            focusedContentColor = cs.onPrimary,
            pressedContainerColor = cs.primary,
            pressedContentColor = cs.onPrimary,
            disabledContainerColor = cs.surface,
            disabledContentColor = cs.onSurface.copy(alpha = 0.5f)
        )
    }
}
