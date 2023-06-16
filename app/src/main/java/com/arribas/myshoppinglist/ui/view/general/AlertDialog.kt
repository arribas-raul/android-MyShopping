package com.arribas.myshoppinglist.ui.view.general

import androidx.compose.foundation.background
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.arribas.myshoppinglist.R

@Composable
fun SimpleAlertDialog(
    show: Boolean,
    title: String,
    body: String = "",
    showDismissButton: Boolean = false,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    if(show) {
        AlertDialog(
            onDismissRequest = onDismiss,

            confirmButton = {
                TextButton(onClick = onConfirm)
                { Text(text = "OK", color = colorResource(R.color.my_primary)) }
            },

            dismissButton = {
                if(showDismissButton) {
                    TextButton(onClick = onDismiss)
                    { Text(text = "Cancel", color = colorResource(R.color.my_primary)) }
                }
            },

            title = { Text(text = title, color = Color.Black) },
            text = { Text(text = body, color = Color.Black) },
            containerColor = colorResource(R.color.my_background)
        )
    }
}