package com.cbuildz.tvpro.ui

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.cbuildz.tvpro.ui.theme.AccentCyan
import com.cbuildz.tvpro.ui.theme.TextPrimary

object TVButtonDefaults {
    @Composable
    fun colors(): ButtonColors {
        val interactionSource = InteractionSource()
        val isFocused by interactionSource.collectIsFocusedAsState()

        return ButtonDefaults.buttonColors(
            containerColor = if (isFocused) AccentCyan.copy(alpha = 0.8f) else Color.DarkGray,
            contentColor = TextPrimary
        )
    }
}
