package com.cbuildz.tvpro

data class Channel(
    val name: String,
    val url: String,
    val logo: String? = null,
    val group: String? = null
)
