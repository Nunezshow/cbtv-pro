package com.cbuildz.tvpro.ui

import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.ButtonDefaults as TvButtonDefaults

object TVButtonDefaults {
    @Composable
    fun colors() = TvButtonDefaults.colors(
        containerColor = Color.DarkGray,
        contentColor = Color.White,
        focusedContainerColor = Color.White,
        focusedContentColor = Color.Black
    )
}
