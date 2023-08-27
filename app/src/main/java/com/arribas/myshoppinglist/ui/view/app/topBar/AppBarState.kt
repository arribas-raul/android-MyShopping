package com.arribas.myshoppinglist.ui.view.app.topBar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class AppBarState(
    var actions: (@Composable RowScope.() -> Unit)? = {
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "",
                tint = Color.White,
            )
        }
    }
)
