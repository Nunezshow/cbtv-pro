package com.cbuildz.tvpro.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.tv.material3.ButtonColors
import androidx.tv.material3.ButtonDefaults

object TVButtonDefaults {
    @Composable
    fun colors(): ButtonColors {
        val cs = MaterialTheme.colorScheme
        return ButtonDefaults.colors(
            containerColor = cs.primary,
            contentColor = cs.onPrimary,
            focusedContainerColor = cs.primary.copy(alpha = 0.95f),
            focusedContentColor = cs.onPrimary,
            pressedContainerColor = cs.primary.copy(alpha = 0.85f),
            pressedContentColor = cs.onPrimary,
            disabledContainerColor = cs.surface,
            disabledContentColor = cs.onSurface.copy(alpha = 0.4f)
        )
    }
}
