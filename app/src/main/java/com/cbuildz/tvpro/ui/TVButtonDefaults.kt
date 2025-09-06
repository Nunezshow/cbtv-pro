package com.cbuildz.tvpro.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ButtonColors

object TVButtonDefaults {
    @Composable
    fun colors(): ButtonColors {
        return ButtonDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            focusedContainerColor = MaterialTheme.colorScheme.primary,
            focusedContentColor = MaterialTheme.colorScheme.onPrimary
        )
    }
}
