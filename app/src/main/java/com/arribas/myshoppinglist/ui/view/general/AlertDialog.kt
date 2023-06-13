package com.arribas.myshoppinglist.ui.view.general

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

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
                { Text(text = "OK") }
            },

            dismissButton = {
                if(showDismissButton) {
                    TextButton(onClick = onDismiss)
                    { Text(text = "Cancel") }
                }
            },

            title = { Text(text = title) },
            text = { Text(text = body) }
        )
    }
}